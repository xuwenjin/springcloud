package com.xwj;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ConditionalOnClass(ZuulFilter.class)
public class PreZuulFilter extends ZuulFilter {

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		// RequestContext.getCurrentContext().addZuulRequestHeader(CloudContext.CONTEXT_ID_KEY,
		// contextId);

		// log.debug("request 中增加 header: {} = {}", CloudContext.CONTEXT_ID_KEY,
		// contextId);
		log.debug("打印日志 ： send %s request to %s", request.getMethod(), request.getRequestURL().toString());
		System.out.println(request.getMethod() + " --- " + request.getRequestURL().toString());
		System.out.println(request.getParameterMap());
		return null;
	}

	/**
	 * 是否开启过滤器
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 1;
	}

	/**
	 * 过滤器的类型，有pre、route、post、error等
	 */
	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

}
