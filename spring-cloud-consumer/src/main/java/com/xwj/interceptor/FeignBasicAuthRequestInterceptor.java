package com.xwj.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * 实现RequestInterceptor拦截器，可以为请求中添加请求头信息
 */
@Slf4j
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				String value = request.getHeader(name);
				requestTemplate.header(name, value);
				log.info("{}: {}", name, value);
			}
		}

		// 添加自定义的header
		requestTemplate.header("Authorization", "123456");
		
		Enumeration<String> bodyNames = request.getParameterNames();
		StringBuffer body = new StringBuffer();
		if (bodyNames != null) {
			while (bodyNames.hasMoreElements()) {
				String name = bodyNames.nextElement();
				String values = request.getParameter(name);
				body.append(name).append("=").append(values).append("&");
			}
		}
		if (body.length() != 0) {
			body.deleteCharAt(body.length() - 1);
			requestTemplate.body(body.toString());
			log.info("feign interceptor body:{}", body.toString());
		}
	}
}