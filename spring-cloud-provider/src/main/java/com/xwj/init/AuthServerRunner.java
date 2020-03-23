package com.xwj.init;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.xwj.properties.KeyConfiguration;
import com.xwj.utils.RSAUtil;

/**
 * 初始化用户私钥秘钥(用于token加解密)
 */
@Configuration
public class AuthServerRunner implements CommandLineRunner {

	private static final String REDIS_USER_PRI_KEY = "AUTH:user:pri";
	private static final String REDIS_USER_PUB_KEY = "AUTH:user:pub";

	@Autowired
	private KeyConfiguration keyConfiguration;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void run(String... args) throws Exception {
		if (redisTemplate.hasKey(REDIS_USER_PRI_KEY) && redisTemplate.hasKey(REDIS_USER_PUB_KEY)) {
			keyConfiguration.setUserPriKey(redisTemplate.opsForValue().get(REDIS_USER_PRI_KEY));
			keyConfiguration.setUserPubKey(redisTemplate.opsForValue().get(REDIS_USER_PUB_KEY));
		} else {
			// 生成用户公钥私钥，并保存到redis
			Map<String, String> keyMap = RSAUtil.generateKey();
			keyConfiguration.setUserPriKey(keyMap.get(RSAUtil.PRI_KEY));
			keyConfiguration.setUserPubKey(keyMap.get(RSAUtil.PUB_KEY));
			redisTemplate.opsForValue().set(REDIS_USER_PRI_KEY, keyMap.get(RSAUtil.PRI_KEY));
			redisTemplate.opsForValue().set(REDIS_USER_PUB_KEY, keyMap.get(RSAUtil.PUB_KEY));
		}
	}

}
