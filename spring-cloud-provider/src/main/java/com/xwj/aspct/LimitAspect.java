package com.xwj.aspct;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.ImmutableList;
import com.xwj.annotations.RequestLimit;
import com.xwj.enums.LimitType;
import com.xwj.exception.LimitException;
import com.xwj.redis.JsonRedisTemplate;
import com.xwj.utils.RequestUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 限流-切面
 */
@Slf4j
@Aspect
@Configuration
public class LimitAspect {

	@Autowired
	private JsonRedisTemplate redisTemplate;

	@Around("execution(public * *(..)) && @annotation(com.xwj.annotations.RequestLimit)")
	public Object interceptor(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		RequestLimit limitAnnotation = method.getAnnotation(RequestLimit.class);
		LimitType limitType = limitAnnotation.limitType();
		String key;
		int limitPeriod = limitAnnotation.period();
		int limitCount = limitAnnotation.count();
		switch (limitType) {
		case IP:
			key = getIpAddress();
			break;
		case CUSTOMER:
			key = limitAnnotation.key();
			break;
		default:
			key = StringUtils.upperCase(method.getName());
		}
		// 合并prefix和key，生成redis中的key
		String sKey = StringUtils.join(limitAnnotation.prefix(), key);
		ImmutableList<String> keys = ImmutableList.of(sKey);
		try {
			String luaScript = buildLuaScript();
			RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
			Number count = redisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
			log.info("Access try count is {} and key = {}", count, key);
			if (count != null && count.intValue() <= limitCount) {
				return pjp.proceed();
			} else {
				throw new LimitException("操作太频繁");
			}
		} catch (Throwable e) {
			if (e instanceof RuntimeException) {
				throw new RuntimeException(e.getLocalizedMessage());
			}
			throw new RuntimeException("server exception");
		}
	}

	/**
	 * 限流脚本
	 *
	 * @return lua脚本
	 */
	public String buildLuaScript() {
		/**
		 * KEYS[1] KEYS[2]，是要操作的键，可以指定多个，在lua脚本中通过KEYS[1], KEYS[2]获取 ARGV[1]
		 * ARGV[2]，参数，在lua脚本中通过ARGV[1], ARGV[2]获取。
		 * 
		 * KEYS[1] = prefix + key = limittest
		 * 
		 * ARGV[1] = count = 3
		 * 
		 * ARGV[2] = period = 10
		 */
		StringBuilder lua = new StringBuilder();
		lua.append("local c");
		lua.append("\nc = redis.call('get', KEYS[1])");
		// 调用不超过最大值，则直接返回
		lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
		lua.append("\nreturn c;");
		lua.append("\nend");
		// 执行计算器自加
		lua.append("\nc = redis.call('incr', KEYS[1])");
		lua.append("\nif tonumber(c) == 1 then");
		// 从第一次调用开始限流，设置对应键值的过期
		lua.append("\nredis.call('expire', KEYS[1], ARGV[2])");
		lua.append("\nend");
		lua.append("\nreturn c;");
		return lua.toString();
	}

	/**
	 * 获取IP地址
	 */
	public String getIpAddress() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return RequestUtil.getRealIpAddr(request);
	}
}