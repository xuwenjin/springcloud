package com.xwj.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.xwj.cache.LoginInfoService;

/**
 * 权限校验
 */
@Configuration
public class AuthConfig implements WebMvcConfigurer {

	@Autowired
	private LoginInfoService loginInfoService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册拦截器
		InterceptorRegistration interceptor = registry.addInterceptor(getSecurityInterceptor());
		interceptor.addPathPatterns("/**");
	}

	// 关键，将拦截器作为bean写入配置中
	@Bean
	public AuthInterceptor getSecurityInterceptor() {
		return new AuthInterceptor(loginInfoService);
	}

}
