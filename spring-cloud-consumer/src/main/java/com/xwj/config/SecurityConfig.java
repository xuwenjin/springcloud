package com.xwj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.xwj.service.MyUserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] exclusivePaths = { "/login", "/home", "/css/**", "/js/**", "/fonts/**",
			"/favicon.ico" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 允许所有人访问exclusivePaths数组中的资源（静态资源、登录的 url）
		http.authorizeRequests().antMatchers(exclusivePaths).permitAll()
				// 除了上一句过滤的请求之外的所有请求都需要认证才能访问
				.anyRequest().authenticated().and()
				// formLogin()是默认的登录表单页，如果不配置 loginPage(url)，则使用 spring security
				// 默认的登录页，如果配置了 loginPage()则使用自定义的登录页
				.formLogin().loginPage("/login")
				// 登录成功之后跳转的页面
				.defaultSuccessUrl("/home");
	}

	/**
	 * 在内存中创建了一个用户，该用户的名称为user，密码为password，用户角色为USER。
	 */
	// @Autowired
	// public void configureGlobal(AuthenticationManagerBuilder auth) throws
	// Exception {
	// auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
	// }

	@Override
	@Bean
	public UserDetailsService userDetailsServiceBean() {
		return new MyUserDetailServiceImpl();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceBean());
	}

}
