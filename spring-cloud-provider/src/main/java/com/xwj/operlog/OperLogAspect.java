package com.xwj.operlog;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import lombok.SneakyThrows;

@Aspect
@Component
public class OperLogAspect {

	@Autowired
	private OperLogService operLogService;

	@Value("${log-init:N}")
	private String logInit; // 是否开启保存操作日志

	// 在有@RestController注解或者方法上加了@MyLog的位置增加切点 @Pointcut
	@Pointcut("@within(org.springframework.web.bind.annotation.RestController) || @annotation(com.xwj.operlog.MyLog)")
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

		OperLog log = new OperLog();
		log.setCreateDate(new Date());
		log.setUsername("xuwenjin");

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		// 获取切入点所在的方法
		Method method = methodSignature.getMethod();

		// 获取操作
		MyLog myLog = method.getAnnotation(MyLog.class);
		if (myLog != null) {
			String value = myLog.value();
			log.setOperation(value); // 操作
		}

		// 获取请求的类名
		String clsName = joinPoint.getTarget().getClass().getName();
		String methodName = method.getName();
		log.setMethod(clsName + "." + methodName);

		// 获取入参类型
		Object[] args = joinPoint.getArgs();
		if (args.length >= 0) {
			String params = Stream.of(args).filter(d -> d != null).map(d -> d.getClass().getName())
					.collect(Collectors.joining(","));
			log.setParamsCls(params);
		}

		// 获取出参类型
		log.setResultCls(method.getReturnType().getName());

		// 获取用户ip地址
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		log.setIp(request.getRemoteAddr());

//		String getContextPath = request.getContextPath();
//		String getRequestURL = request.getRequestURL().toString();

		operLogService.saveLog(log);
	}

}
