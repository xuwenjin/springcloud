package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // 开启异步调用
@EnableRetry // 开始重试
// @SpringBootApplication
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.xwj"
, excludeFilters = { 
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.xwj.test\\.*"),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.xwj.controller.*")
		}
)
@ServletComponentScan // @ServletComponentScan扫描带@WebFilter、@WebServlet、@WebListener并将帮我们注入bean
@EnableDiscoveryClient
public class ProviderApplication {

	public static void main(String[] args) {
		/** 配置加解密秘钥，与配置文件的密文分开放 */
		System.setProperty("jasypt.encryptor.password", "travel-app");
		// System.setProperty("jasypt.encryptor.password",
		// "EbfYkitulv73I2p0mXI50JMXoaxZTKJ7");

		SpringApplication.run(ProviderApplication.class, args);
	}

}
