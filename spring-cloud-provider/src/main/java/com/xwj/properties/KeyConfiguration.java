package com.xwj.properties;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * key的配置
 */
@Configuration
@Data
public class KeyConfiguration {

	private String userPubKey; // 用户公钥
	private String userPriKey; // 用户私钥

}
