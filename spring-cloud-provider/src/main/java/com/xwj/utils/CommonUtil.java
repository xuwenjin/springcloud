package com.xwj.utils;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 公共工具类
 */
@Slf4j
public class CommonUtil {

	/**
	 * 生成随机16位key
	 */
	public static String generateKey() {
		return RandomStringUtils.randomAlphanumeric(16);
	}

	public static String generateVerificationCode() {
		return RandomStringUtils.random(2, "123456789");
	}

	/**
	 * 校验时间戳是否过期
	 * 
	 * @param timestamp
	 *            时间戳(毫秒)
	 * @param time
	 *            有效时长(毫秒)
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

}
