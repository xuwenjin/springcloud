package com.xwj.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xwj.properties.LoginType;
import com.xwj.properties.SecurityProperty;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义登录失败后处理 
 * (继承SimpleUrlAuthenticationFailureHandler，这是默认的失败处理器)
 */
@Slf4j
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityProperty securityProperty;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("登录失败");

		if (LoginType.JSON.equals(securityProperty.getBrowser().getLoginType())) { // json请求(如ajax)
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); // 服务器内部异常500
			response.setContentType("application/json;charset=UTF-8");
			// 返回异常信息
			response.getWriter().write(objectMapper.writeValueAsString(exception));
		} else {
			super.onAuthenticationFailure(request, response, exception);
		}
	}

}
