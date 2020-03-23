package com.xwj.auth;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${jwt.expire:3600}")
	private int expire; // token失效时间
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

	/**
	 * 通过用户名查询token
	 */
	public String getJwtToken(String username) {
		String redisKey = CommonUtil.buildRedisKey(RedisKeys.USER_INFO, username);
		return (String) redisTemplate.opsForValue().get(redisKey);
	}

	/**
	 * 通过用户名查询token是否存在
	 */
	public boolean hasJwtToken(String username) {
		String redisKey = CommonUtil.buildRedisKey(RedisKeys.USER_INFO, username);
		return redisTemplate.hasKey(redisKey);
	}

	/**
	 * 将token放在redis
	 */
	public void setJwtToken(String username, String token) {
		String redisKey = CommonUtil.buildRedisKey(RedisKeys.USER_INFO, username);
		redisTemplate.opsForValue().set(redisKey, token, expire, TimeUnit.SECONDS);
	}

}
