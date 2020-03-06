package com.xwj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xwj.interceptor.AuthInterceptor;

/**
 * 配置类-拦截请求
 */
@Configuration
public class AuthWebConfig implements WebMvcConfigurer {

	@Autowired
	private AuthInterceptor authInterceptor;

	/**
	 * 增加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册拦截器
		registry.addInterceptor(authInterceptor).addPathPatterns("/**");
	}

}
