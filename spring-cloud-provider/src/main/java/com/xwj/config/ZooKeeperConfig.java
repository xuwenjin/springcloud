package com.xwj.config;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ZooKeeper配置类
 */
@Configuration
public class ZooKeeperConfig {

	/**
	 * 配置ZooKeeper
	 */
	@Bean
	public ZkClient zkClient() {
		return new ZkClient("localhost:2181", 1000, 1000, new SerializableSerializer());
	}

}
