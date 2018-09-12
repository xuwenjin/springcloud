package com.xwj.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class JsonRedisTemplate extends RedisTemplate<String, Object> {
	
	public JsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		super.setConnectionFactory(redisConnectionFactory);
	}

}
