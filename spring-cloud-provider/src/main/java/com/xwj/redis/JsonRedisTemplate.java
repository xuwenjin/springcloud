package com.xwj.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 序列化RedisTemplate中的键值
 * 
 * @author xwj
 *
 */
public class JsonRedisTemplate extends RedisTemplate<String, Object> {

	public JsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

		GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(mapper);
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		setKeySerializer(stringSerializer);
		setValueSerializer(jsonSerializer);
		setHashKeySerializer(stringSerializer);
		setHashValueSerializer(jsonSerializer);
		setConnectionFactory(redisConnectionFactory);
	}

}
