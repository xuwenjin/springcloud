package com.xwj.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
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

	/**
	 * 手写redis锁的配置类
	 */
	@Bean
	public JedisPool jedisPoolFactory() {
		JedisPool jp = new JedisPool(redisProperties.getHost(), redisProperties.getPort());
		return jp;
	}

	/**
	 * JsonRedisTemplate配置
	 */
	@Bean
	public JsonRedisTemplate jsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		return new JsonRedisTemplate(redisConnectionFactory);
	}

	/**
	 * 单机版Redisson配置
	 */
	@Bean
	public RedissonClient redissonSingle() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
				.setDatabase(1);
		return Redisson.create(config);
	}

}
