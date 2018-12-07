package com.xwj.operlog;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Aspect
@Component
public class OperLogAspect {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OperLogService operLogService;

	// 定义切点 @Pointcut
	// 在注解的位置切入代码
	// @Pointcut("@annotation(com.xwj.operlog.MyLog)")
	// @Pointcut("@within(org.springframework.stereotype.Service)")
	@Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
	public void logPoinCut() {
	}

	// 切面 配置通知
	@SneakyThrows
	@AfterReturning("logPoinCut()")
	public void saveSysLog(JoinPoint joinPoint) {
		OperLog log = new OperLog();

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		// 获取切入点所在的方法
		Method method = methodSignature.getMethod();

		// 获取操作
		// MyLog myLog = method.getAnnotation(MyLog.class);
		// if (myLog != null) {
		// String value = myLog.value();
		// log.setOperation(value); // 操作
		// }

		// 获取请求的类名
		String clsName = joinPoint.getTarget().getClass().getName();
		String methodName = method.getName();
		log.setMethod(clsName + "." + methodName);

		// 获取请求的参数
		Object[] args = joinPoint.getArgs();
		String params = objectMapper.writeValueAsString(args);
		log.setParams(params);

		log.setCreateDate(new Date());
		log.setUsername("xuwenjin");

		// 获取用户ip地址
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		log.setIp(request.getRemoteAddr());

		operLogService.saveLog(log);
	}

}
