package com.xwj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IRule;
import com.xwj.ribbonrule.GlobalRule;

/**
 * 配置Ribbon(也可以不配置，这里是为了更改ribbon的默认负载均衡策略)
 */
@Configuration
public class MyRibbonConfig {

	@Bean
	public IRule myRule() {
		// ribbon提供的策略：随机选择一个服务实例
		// IRule rule = new RandomRule();

		// 自定义策略
		IRule rule = new GlobalRule();

		return rule;
	}

}
