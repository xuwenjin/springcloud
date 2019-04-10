package com.xwj.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.redis") 
public class RedisProperties {

	private String host;

	private int port;

	private int timeout; // 秒

	private String password;

	private int poolMaxTotal; //最大连接数

	private int poolMaxIdle; //最大空闲数
	
	private int poolMaxWait; // 秒

}
