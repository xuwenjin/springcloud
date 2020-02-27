package com.xwj.advice;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xwj.annotations.IgnoreEncode;
import com.xwj.interceptor.AuthUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 对response统一处理
 */
@Slf4j
@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(MethodParameter methodParameter, Class clazz) {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class clazz,
			ServerHttpRequest request, ServerHttpResponse httpResponse) {
		Method method = methodParameter.getMethod();
		if (AuthUtil.security.get()) {// 启用加密传输
			if (method.isAnnotationPresent(IgnoreEncode.class)) {
				// 强制不加密
				return obj;
			}

			String appId = request.getHeaders().getFirst("AppId");// 传入APPID来决定启用哪个秘钥对
			if (StringUtils.isEmpty(appId)) {
				appId = AuthUtil.defaultAppId;
			}
			if (AuthUtil.rsaKeyMap.containsKey(appId)) {
				return EncryptHttpOutputMessage.getResponse(obj, httpResponse, appId);
			}
		}
		log.info("返回参数未加密:{}", JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue));
		return obj;
	}
}
