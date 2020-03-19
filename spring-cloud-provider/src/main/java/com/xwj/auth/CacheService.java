package com.xwj.auth;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.common.AuthConsts;
import com.xwj.common.RedisKeys;
import com.xwj.redis.JsonRedisTemplate;
import com.xwj.utils.CommonUtil;

/**
 * 缓存到redis
 */
@Service
public class CacheService {

	@Autowired
	private JsonRedisTemplate redisTemplate;

	/**
	 * 设置黑名单
	 */
	public void addBlackIp(String blackIp) {
		if (StringUtils.isNotBlank(blackIp)) {
			String redisKey = CommonUtil.buildRedisKey(RedisKeys.BLACK_LIMIT, blackIp);
			// 默认一天过期
			redisTemplate.opsForValue().set(redisKey, blackIp, AuthConsts.BLACK_TIMEOUT, TimeUnit.HOURS);
		}
	}

	/**
	 * 是否包含黑名单
	 */
	public boolean containBlackIp(String blackIp) {
		if (StringUtils.isBlank(blackIp)) {
			return false;
		}
		String redisKey = CommonUtil.buildRedisKey(RedisKeys.BLACK_LIMIT, blackIp);
		return redisTemplate.hasKey(redisKey);
	}

	/**
	 * 删除黑名单
	 */
	public void removeBlackIp(String blackIp) {
		if (StringUtils.isNotBlank(blackIp)) {
			String redisKey = CommonUtil.buildRedisKey(RedisKeys.BLACK_LIMIT, blackIp);
			boolean has = redisTemplate.hasKey(redisKey);
			if (has) {
				redisTemplate.delete(redisKey);
			}
		}
	}

}
