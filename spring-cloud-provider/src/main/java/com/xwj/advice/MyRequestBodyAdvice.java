package com.xwj.advice;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.xwj.annotations.IgnoreEncode;
import com.xwj.auth.AuthUtil;
import com.xwj.utils.CommonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 对request统一参数处理
 * 
 * RequestBodyAdvice仅对使用了@RequestBody注解的生效 , 因为它原理上还是AOP , 所以GET方法是不会操作的
 */
@Slf4j
@ControllerAdvice
public class MyRequestBodyAdvice extends RequestBodyAdviceAdapter {

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		// 仅对使用了@RequestBody注解的生效
		return methodParameter.hasParameterAnnotation(RequestBody.class);
	}

	/**
	 * 读取参数前执行
	 * 
	 * 在此做些编码 / 解密 / 封装参数为对象的操作
	 */
	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		Method method = parameter.getMethod();
		try {
			if (method.isAnnotationPresent(IgnoreEncode.class)) {
				// 带这个注解的方法就不启用加密，即使加密已经启动
				return new CommonHttpInputMessage(inputMessage);
			}
			if (AuthUtil.security.get()) {// 启用加密传输
				String appId = CommonUtil.getAppId(inputMessage.getHeaders().getFirst("AppId"));// 传入APPID来决定启用哪个秘钥对
				if (AuthUtil.rsaKeyMap.containsKey(appId)) {
					return new DecryptHttpInputMessage(inputMessage, appId);
				}
			}
			return new CommonHttpInputMessage(inputMessage);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return inputMessage;
	}

	/**
	 * 读取参数后执行
	 */
	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
	}

	/**
	 * 无请求时的处理
	 */
	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return super.handleEmptyBody(body, inputMessage, parameter, targetType, converterType);
	}

}
