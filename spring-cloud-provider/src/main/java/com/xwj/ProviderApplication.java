package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableRetry // 开始重试
@SpringBootApplication
@ServletComponentScan // @ServletComponentScan扫描带@WebFilter、@WebServlet、@WebListener并将帮我们注入bean
// @EnableDiscoveryClient
public class ProviderApplication {

	public static void main(String[] args) {
		/** 配置加解密秘钥，与配置文件的密文分开放 */
		System.setProperty("jasypt.encryptor.password", "travel-app");
		// System.setProperty("jasypt.encryptor.password",
		// "EbfYkitulv73I2p0mXI50JMXoaxZTKJ7");

		SpringApplication.run(ProviderApplication.class, args);
	}

}
