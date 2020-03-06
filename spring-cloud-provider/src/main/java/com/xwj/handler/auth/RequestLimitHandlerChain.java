package com.xwj.handler.auth;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.xwj.annotations.RequestLimit;
import com.xwj.common.ApiResponseData;
import com.xwj.common.RedisKeys;
import com.xwj.handler.AbstractHandler;
import com.xwj.handler.HandlerChain;
import com.xwj.redis.JsonRedisTemplate;
import com.xwj.utils.MD5Util;
import com.xwj.utils.RequestUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 请求限流
 */
@Slf4j
@Component
public class RequestLimitHandlerChain extends AbstractHandler {

	@Autowired
	private JsonRedisTemplate redisTemplate;

	@Override
	public boolean isSupport(HandlerMethod handlerMethod) {
		Method method = handlerMethod.getMethod();
		return method.isAnnotationPresent(RequestLimit.class);
	}

	@Override
	public ApiResponseData handleAuth(HandlerChain chain, HandlerMethod handlerMethod, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("校验请求限流");
		try {
			Method method = handlerMethod.getMethod();
			RequestLimit limitAnnotation = method.getAnnotation(RequestLimit.class);
			long limitPeriod = limitAnnotation.period();
			long limitCount = limitAnnotation.count();

			// 以用户id+请求路径+请求参数作为key
			String redisKey = this.getRequestRedisKey(method, request);
			ImmutableList<String> keys = ImmutableList.of(redisKey);
			String luaScript = buildLuaScript();
			RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
			Number count = redisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
			log.info("request limit: count = {}, key = {}", count, redisKey);
			if (count != null && count.intValue() > limitCount) {
				return ApiResponseData.unauthorizedError("操作太频繁");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ApiResponseData.innerError("系统错误");
		}

		return chain.handleAuth(chain, handlerMethod, request, response);
	}

	/**
	 * 限流脚本
	 *
	 * @return lua脚本
	 */
	public String buildLuaScript() {
		/**
		 * KEYS[1] KEYS[2]，是要操作的键，可以指定多个，在lua脚本中通过KEYS[1], KEYS[2]获取
		 * 
		 * ARGV[1] ARGV[2]，参数，在lua脚本中通过ARGV[1], ARGV[2]获取。
		 * 
		 * KEYS[1] = prefix + key
		 * 
		 * ARGV[1] = count = 3
		 * 
		 * ARGV[2] = period = 10
		 */
		StringBuilder lua = new StringBuilder();
		lua.append("local c");
		lua.append("\nc = redis.call('get', KEYS[1])");
		// 调用超过最大值，则直接返回
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
	 * 获取redis键(由以用户id+请求路径+请求参数组成)
	 */
	private String getRequestRedisKey(Method method, HttpServletRequest request) {
		StringBuilder key = new StringBuilder();
		key.append(RequestUtil.getRealIpAddr(request));
		key.append(request.getRequestURI());

		// 请求参数
		Parameter[] params = method.getParameters();
		if (ArrayUtils.isNotEmpty(params)) {
			key.append(JSON.toJSONString(params));
		}

		String md5Key = MD5Util.md5(key.toString());
		return StringUtils.join(RedisKeys.REQUEST_LIMIT, md5Key);
	}

}
