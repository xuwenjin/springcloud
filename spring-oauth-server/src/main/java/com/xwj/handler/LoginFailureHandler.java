package com.xwj.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义登录失败后处理
 */
@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("登录失败");
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); // 服务器内部异常500
		response.setContentType("application/json;charset=UTF-8");
		// 返回异常信息
		response.getWriter().write(objectMapper.writeValueAsString(exception));
	}

}
