package com.xwj.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xwj.properties.LoginType;
import com.xwj.properties.SecurityProperty;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义登录成功后处理
 * (继承SavedRequestAwareAuthenticationSuccessHandler，这是默认的成功处理器，其带有requestCache)
 */
@Slf4j
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityProperty securityProperty;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("登录成功...");

		if (LoginType.JSON.equals(securityProperty.getBrowser().getLoginType())) { // json请求(如ajax)
			response.setContentType("application/json;charset=UTF-8");
			// 返回authentication 认证信息
			response.getWriter().write(objectMapper.writeValueAsString(authentication));
		} else { // 跳转
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}

}
