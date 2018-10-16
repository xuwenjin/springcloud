package com.xwj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.xwj.consts.SecurityConst;
import com.xwj.properties.SecurityProperty;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperty.class) // 开启配置文件
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private SecurityProperty securityProperty;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// formLogin()是默认的登录表单页，如果不配置 loginPage(url)，则使用 spring security
		// 默认的登录页，如果配置了 loginPage()则使用自定义的登录页
		http.formLogin() // 表单登录
			.loginPage(SecurityConst.AUTH_REQUIRE) 
			.loginProcessingUrl(SecurityConst.AUTH_FORM) // 登录请求拦截的url,也就是form表单提交时指定的action
			.and()
			.authorizeRequests() // 对请求授权
			.antMatchers(SecurityConst.AUTH_REQUIRE, securityProperty.getBrowser().getLoginPage()).permitAll() // 允许所有人访问login.html和自定义的登录页
			.anyRequest() // 任何请求
			.authenticated()// 需要身份认证
			.and()
			.csrf().disable() // 关闭跨站伪造
		;
	}

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.formLogin() // 表单登录。跳转到security默认的登录表单页
	// // http.httpBasic() //basic登录
	// .and()
	// .authorizeRequests() // 对请求授权
	// .antMatchers("/formLogin").permitAll() //请求/formLogin不需要认证
	// .anyRequest() // 任何请求
	// .authenticated()// 需要身份认证
	// ;
	// }

	/**
	 * 密码加密(可自定义加密方式)
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
