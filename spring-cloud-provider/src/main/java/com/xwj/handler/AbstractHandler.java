package com.xwj.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;

import com.xwj.common.ApiResponseData;

/**
 * 责任链模式
 */
public abstract class AbstractHandler {

	public boolean isSupport(HandlerMethod handlerMethod) {
		return true;
	}

	public abstract ApiResponseData handleAuth(HandlerChain chain, HandlerMethod handlerMethod,
			HttpServletRequest request, HttpServletResponse response);

}
