package com.xwj.operlog;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.xwj.annotations.OperLogAnn;
import com.xwj.entity.OperLog;
import com.xwj.utils.RequestUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class OperLogAspect {

	@Autowired
	private OperLogService operLogService;

	@Value("${log-init:N}")
	private String logInit; // 是否开启保存操作日志

	// 在有@RestController注解或者方法上加了@OperLogAnn的位置增加切点 @Pointcut
	@Pointcut("@annotation(com.xwj.annotations.OperLogAnn)")
	public void logPoinCut() {
	}

	// 切面 配置通知
	@SneakyThrows
	@AfterReturning(returning = "retObj", pointcut = "logPoinCut()")
	public void saveOperLog(JoinPoint joinPoint, Object retObj) {
		// 如果没有开启保存操作日志，直接返回
		if (StringUtils.equals("N", logInit)) {
			return;
		}

		OperLog operLog = new OperLog();
		operLog.setUserId("123");
		operLog.setUsername("xuwenjin");

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		// 获取切入点所在的方法
		Method method = methodSignature.getMethod();

		// 获取操作
		OperLogAnn operLogAnn = method.getAnnotation(OperLogAnn.class);
		if (operLogAnn != null) {
			operLog.setOperation(operLogAnn.value());
			operLog.setOperationModule(operLogAnn.operModule());
		}

		// 获取请求的类名
		String clsName = joinPoint.getTarget().getClass().getName();
		String methodName = method.getName();
		operLog.setMethod(clsName + "." + methodName);

		// 获取请求参数
		Object[] args = joinPoint.getArgs();
		if (args.length > 0) {
			operLog.setRequestParam(JSON.toJSONString(args));
		}

		// 获取响应结果
		operLog.setResponseResult(JSON.toJSONString(retObj));

		// 获取用户ip地址
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		operLog.setIp(RequestUtil.getRealIpAddr(request));
		operLog.setPath(request.getRequestURI()); // 请求URI

		try {
			operLogService.saveLog(operLog);
		} catch (Exception e) {
			log.error("保存操作日志失败", e);
		}
	}

}
