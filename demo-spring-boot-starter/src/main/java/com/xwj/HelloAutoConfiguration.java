package com.xwj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(HelloProperties.class)
public class HelloAutoConfiguration {

	/**
	 * ConditionalOnWebApplication注解：为web服务时，才使注解的类生效
	 * 
	 * EnableConfigurationProperties注解：用来开启对HelloProperties中的@ConfigurationProperties注解配置Bean的支持。
	 * 也就是@EnableConfigurationProperties注解告诉SpringBoot能支持@ConfigurationProperties。
	 */

	@Autowired
	private HelloProperties helloProperties;

	@Bean
	public HelloService helloService() {
		HelloService service = new HelloService();
		service.setHelloProperties(helloProperties);
		return service;
	}

}
