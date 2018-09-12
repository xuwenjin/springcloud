package com.xwj.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "redis") // 将配置文件中以redis开头的配置项赋值到该类的属性
public class RedisProperties {

	private String host;

	private int port;

	private int timeout; // 秒

	private String password;

	private int poolMaxTotal; //最大连接数

	private int poolMaxIdle; //最大空闲数
	
	private int poolMaxWait; // 秒

}
