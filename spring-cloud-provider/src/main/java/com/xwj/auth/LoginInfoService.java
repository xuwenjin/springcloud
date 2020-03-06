package com.xwj.auth;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import com.xwj.common.AppUserLogVO;
import com.xwj.common.RedisKeys;

import lombok.extern.slf4j.Slf4j;

/**
 * Redis 服务实现类
 * 
 * 用户状态数据存到redis中，用于控制app的访问
 */
@Slf4j
@Service
public class LoginInfoService {

	// 使用LRU缓存算法减少redis的访问次数
	private static ConcurrentLinkedHashMap<String, Map<String, String>> LRUCache = new ConcurrentLinkedHashMap.Builder<String, Map<String, String>>()
			.maximumWeightedCapacity(500).weigher(Weighers.singleton()).build();

	// 使用LRU缓存算法减少redis的访问次数
	private static ConcurrentLinkedHashMap<String, Map<String, String>> LastLRUCache = new ConcurrentLinkedHashMap.Builder<String, Map<String, String>>()
			.maximumWeightedCapacity(500).weigher(Weighers.singleton()).build();

	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 获取redis中用户信息
	 */
	public Map<String, String> getLoginInfo(String accessToken) {
		try {
			Map<String, String> reulst = LRUCache.get(accessToken);
			if (null != reulst) {
				return reulst;
			}
			boolean has = redisTemplate.opsForHash().hasKey(RedisKeys.LOGIN_INFO, accessToken);
			if (has) {
				String json = (String) redisTemplate.opsForHash().get(RedisKeys.LOGIN_INFO, accessToken);
				reulst = JSON.parseObject(json, new TypeReference<Map<String, String>>() {
				});
				LRUCache.put(accessToken, reulst);
				return reulst;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 将用户信息添加到redis中
	 */
	public void setLoginInfo(String accessToken, Map<String, String> account) {
		try {
			String jsonAccount = JSON.toJSONString(account);
			LRUCache.put(accessToken, account);
			redisTemplate.opsForHash().put(RedisKeys.LOGIN_INFO, accessToken, jsonAccount);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 设置请求信息
	 */
	public void setRequestInfo(String key) {
		try {
			boolean has = redisTemplate.hasKey(key);
			if (has) {
				// 将值加1
				redisTemplate.boundValueOps(key).increment(1);
			} else {// 300秒失效
				redisTemplate.opsForValue().set(key, "0", 300, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取请求信息
	 */
	public String getRequestInfo(String key) {
		try {
			return redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 删除添加到redis中的用户信息
	 */
	public void removeLoginInfo(String accessToken) {
		try {
			LRUCache.remove(accessToken);
			redisTemplate.opsForHash().delete(RedisKeys.LOGIN_INFO, accessToken);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 只存最新的用户登录信息
	 */
	public void setLastLoginInfo(String mobile, Map<String, String> account) {
		try {
			String jsonAccount = JSON.toJSONString(account);
			LastLRUCache.put(mobile, account);
			redisTemplate.opsForHash().put(RedisKeys.LAST_LOGIN_INFO, mobile, jsonAccount);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取最近一次用户登录信息
	 */
	public Map<String, String> getLastLoginInfo(String mobile) {
		try {
			Map<String, String> reulst = LastLRUCache.get(mobile);
			if (null != reulst) {
				return reulst;
			}
			boolean has = redisTemplate.opsForHash().hasKey(RedisKeys.LAST_LOGIN_INFO, mobile);
			if (has) {
				String json = (String) redisTemplate.opsForHash().get(RedisKeys.LAST_LOGIN_INFO, mobile);
				reulst = JSON.parseObject(json, new TypeReference<Map<String, String>>() {
				});
				LastLRUCache.put(mobile, reulst);
				return reulst;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 移除用户登录信息 删除添加到redis中的用户信息
	 */
	public void removeLastLoginInfo(String mobile) {
		try {
			LastLRUCache.remove(mobile);
			redisTemplate.opsForHash().delete(RedisKeys.LAST_LOGIN_INFO, mobile);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 判断是否越权访问（逻辑根据mobile获取AccessToken，对比当前accessToken和AccessToken是否一致）
	 * 如果不一致，说明越权访问
	 */
	public boolean checkPermission(String mobile, String accessToken) {
		try {
			if (!AuthUtil.auth.get()) {// 关闭鉴权
				return true;
			}
			// 判断accessToken是否已经存在
			// 从redis中获取用户信息
			Map<String, String> loginInfo = getLoginInfo(accessToken);
			if (null == loginInfo) {
				// removeLoginInfo(accessToken);
				log.error("用户未登陆->loginInfo:" + mobile + ";accessToken:" + accessToken);
				return false;
			}
			String loginType = loginInfo.get("loginType");
			Map<String, String> lastLoginInfo = null;
			if (null == loginType || loginType.length() == 0) {
				lastLoginInfo = getLastLoginInfo(mobile);// 如果登录类型是空说明是e约车
			} else {
				lastLoginInfo = getLastLoginInfo(mobile + loginType);// 如果登录类型是空说明是e约车
			}
			if (lastLoginInfo == null) {
				log.error("用户未登陆->lastLoginInfo:" + mobile + ";accessToken:" + accessToken);
				return false;
			}
			String lastAccessToken = lastLoginInfo.get("AccessToken");
			if (accessToken.equals(lastAccessToken)) {
				return true;
			} else {
				log.error("mobile:" + mobile + ",accessToken:" + accessToken + ",lastAccessToken:" + lastAccessToken
						+ "之前登录已经被下线,本次访问越权");
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.error("mobile:" + mobile + ",accessToken:" + accessToken + "越权访问");
		return false;
	}

	/**
	 * RedisKeys.LOGIN_INFO中的废数据会越来越多
	 */
	public void clearLoginInfo() {
		try {
			// 清除内存中的登录信息
			LastLRUCache.clear();
			LRUCache.clear();
			// 删除所有登录信息
			redisTemplate.delete(RedisKeys.LOGIN_INFO);
			// 删除最新登录信息
			redisTemplate.delete(RedisKeys.LAST_LOGIN_INFO);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 设置用户日志
	 */
	public void setUserLog(String phone, String accessToken, String userIp, String url, String type, String msg) {
		try {
			if (StringUtils.isEmpty(phone)) {
				phone = "未知";
			}
			AppUserLogVO appUserLog = new AppUserLogVO();
			appUserLog.setMobile(phone);
			appUserLog.setIp(userIp);
			appUserLog.setToken(accessToken);
			appUserLog.setUrl(url);
			appUserLog.setActionType(type);
			appUserLog.setActionTime(new Date());
			appUserLog.setActionResult(msg);
			log.info("用户日志：{}", appUserLog);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
