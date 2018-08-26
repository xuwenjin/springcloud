package com.xwj.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.properties.SecurityProperty;
import com.xwj.support.SimpleResponse;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BrowserSecurityController {

	private RequestCache requestCache = new HttpSessionRequestCache(); // 请求缓存

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();// 页面跳转

	@Autowired
	private SecurityProperty securityProperty;
	
	/**
	 * 当需要身份认证时，跳转到这里
	 * (如果是html请求，则跳转到登录页，否则返回401状态码和错误信息)
	 */
	@SneakyThrows
	@RequestMapping("/authentication/require")
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED) // 未授权状态码401
	public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) {
		// 从session中取之前缓存的请求
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();
			log.info("引发跳转的请求是:" + targetUrl);
			if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
				// 跳转到指定的页面
				redirectStrategy.sendRedirect(request, response, securityProperty.getBrowser().getLoginPage());
			}
		}
		return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
	}

}
