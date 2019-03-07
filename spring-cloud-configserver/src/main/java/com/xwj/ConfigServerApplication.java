package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * config服务端
 * 
 * @author xuwenjin 2019年2月27日
 */
@SpringBootApplication
@EnableConfigServer // 通过@EnableConfigServer注解激活配置服务
@EnableDiscoveryClient // 开启服务发现
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
