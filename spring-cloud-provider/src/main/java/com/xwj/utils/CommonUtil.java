package com.xwj.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.xwj.auth.AuthUtil;
import com.xwj.common.AuthConsts;
import com.xwj.common.RsaKey;

import lombok.extern.slf4j.Slf4j;

/**
 * 公共工具类
 */
@Slf4j
public class CommonUtil {

	/**
	 * 生成随机16位key(包含大小写字母和数字)
	 */
	public static String generateKey() {
		return RandomStringUtils.randomAlphanumeric(16);
	}

	/**
	 * 生成指定位数的随机数(只包含1-9)
	 */
	public static String generateNumKey(final int count) {
		return RandomStringUtils.random(count, "123456789");
	}

	/**
	 * 校验时间戳是否过期
	 * 
	 * @param timestamp 时间戳(毫秒)
	 * @param time      有效时长(毫秒)
	 */
	public static boolean checkTimestamp(long timestamp, long time) {
		long nowTimeStamp = System.currentTimeMillis();
		long offset = Math.abs(nowTimeStamp - timestamp);
		if (offset > time) {
			log.info("时间间隔：{}，有效时长：{}", offset, time);
			return false;
		}
		return true;
	}

	/**
	 * 校验时间戳是否过期(默认60秒)
	 * 
	 * @param timestamp 时间戳(毫秒)
	 */
	public static boolean checkTimestamp(long timestamp) {
		return checkTimestamp(timestamp, AuthConsts.REQUEST_TIMEOUT);
	}

	/**
	 * 使用RSA+AES解密
	 */
	public static JSONObject decrpt(String key, String data, String appId) {
		if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(data)) {
			// 通过appId获取RSA秘钥
			RsaKey rsaKey = AuthUtil.rsaKeyMap.get(appId);
			// 通过RSA私钥解密，得到AES的秘钥
			String aesKey = RSAUtil.privateDecrypt(key, rsaKey.getRequestPrivateKey());
			// 通过AES秘钥，得到真实的请求数据
			String json = AESUtil.decrypt(data, aesKey);
			return JSONObject.parseObject(json);
		}
		return null;
	}

	/**
	 * 获取appId(默认DEFAULT_APPID)
	 */
	public static String getAppId(String appId) {
		if (StringUtils.isEmpty(appId)) {
			appId = AuthConsts.DEFAULT_APPID;
		}
		return appId;
	}

}
