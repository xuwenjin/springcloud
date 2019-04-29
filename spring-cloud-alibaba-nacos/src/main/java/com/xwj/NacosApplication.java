package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Spring Cloud Alibaba系列-nacos使用
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosApplication.class, args);
	}

}
