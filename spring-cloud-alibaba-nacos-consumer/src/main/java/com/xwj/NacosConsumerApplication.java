package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Spring Cloud Alibaba系列-nacos使用(服务消费者)
 */
@SpringBootApplication
@EnableFeignClients // 开启feign
public class NacosConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosConsumerApplication.class, args);
	}

}
