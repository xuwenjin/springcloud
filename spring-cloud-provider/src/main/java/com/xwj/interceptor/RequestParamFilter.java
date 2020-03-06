package com.xwj.interceptor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 请求参数过滤器(一次请求只通过一次filter，而不需要重复执行)
 */
@WebFilter(filterName = "paramFilter", urlPatterns = "/*") // 过滤器拦截所有请求
@Order(100)
public class RequestParamFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest wrapper = new RequestWrapper(request);
		filterChain.doFilter(wrapper, response);
	}

}