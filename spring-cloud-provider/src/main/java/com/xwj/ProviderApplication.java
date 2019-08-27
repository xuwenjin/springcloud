package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry // 开始重试
@SpringBootApplication
//@EnableDiscoveryClient
public class ProviderApplication {

	public static void main(String[] args) {
		/** 配置加解密秘钥，与配置文件的密文分开放 */
		System.setProperty("jasypt.encryptor.password", "travel-app");
//		System.setProperty("jasypt.encryptor.password", "EbfYkitulv73I2p0mXI50JMXoaxZTKJ7");

		SpringApplication.run(ProviderApplication.class, args);
	}

}
