package com.xwj.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import redis.clients.jedis.JedisPool;

/**
 * redis配置
 * 
 * @author xuwenjin
 */
@Configuration
public class RedisConfig {

	@Autowired
	private RedisProperties redisProperties;

	@Bean
	public JedisPool jedisPoolFactory() {
		JedisPool jp = new JedisPool(redisProperties.getHost(), redisProperties.getPort());
		return jp;
	}

	@Bean
	public JsonRedisTemplate jsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		return new JsonRedisTemplate(redisConnectionFactory);
	}

}
