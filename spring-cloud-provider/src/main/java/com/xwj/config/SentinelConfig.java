package com.xwj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;

/**
 * Sentinel配置类
 */
@Configuration
public class SentinelConfig {

	/**
	 * 配置Sentinel限流切面
	 */
	@Bean
	public SentinelResourceAspect sentinelResourceAspect() {
		return new SentinelResourceAspect();
	}

}
