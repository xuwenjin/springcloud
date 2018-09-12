package com.xwj.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(redisProperties.getPoolMaxIdle());
		poolConfig.setMaxTotal(redisProperties.getPoolMaxTotal());
		poolConfig.setMaxWaitMillis(redisProperties.getPoolMaxWait() * 1000);
		JedisPool jp = new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort(),
				redisProperties.getTimeout() * 1000);
		return jp;
	}

	@Bean
	public JsonRedisTemplate jsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		return new JsonRedisTemplate(redisConnectionFactory);
	}

}
