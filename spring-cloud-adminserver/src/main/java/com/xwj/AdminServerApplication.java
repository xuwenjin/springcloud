package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

/**
 * admin监控服务器
 */
@SpringBootApplication
@EnableAdminServer // 开启监控。利用Spring Boot Admin 来监控各个独立Service的运行状态
@EnableDiscoveryClient // 开启服务发现
public class AdminServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminServerApplication.class, args);
	}

}
