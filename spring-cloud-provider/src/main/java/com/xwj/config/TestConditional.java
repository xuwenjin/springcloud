package com.xwj.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xwj.service.test.IBaseService;
import com.xwj.service.test.LocalServiceImpl;

/**
 * 测试注解@ConditionalOnMissingBean和@ConditionalOnBean
 */
@Configuration
public class TestConditional {

	/**
	 * 注解@ConditionalOnBean：当给定的类型、类名、注解在beanFactory中存在时返回true
	 * 
	 * 本例中，如果在spring容器中，找到了IBaseService的实现类，则用HDFSServiceImpl作为其实现类并加入到spring容器中
	 */
	// @Bean
	// @ConditionalOnBean
	// public IBaseService baseService() {
	// return new HDFSServiceImpl();
	// }

	/**
	 * 注解@ConditionalOnMissingBean：当给定的类型、类名、注解在beanFactory中不存在时返回true
	 * 
	 * 本例中，如果在spring容器中，没有找到IBaseService的实现类，则用LocalServiceImpl作为其实现类并加入到spring容器中
	 */
	@Bean
	@ConditionalOnMissingBean
	public IBaseService baseService() {
		return new LocalServiceImpl();
	}

}
