package com.xwj.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

import lombok.extern.slf4j.Slf4j;

/**
 * md5工具类
 */
@Slf4j
public class MD5Util {

	/**
	 * md5加密
	 * 
	 * @param str
	 *            被加密的字符
	 */
	public static String md5(String data) {
		if (data == null)
			throw new RuntimeException("md5加密参数不允许为null");
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bytes = md5.digest(data.getBytes("UTF-8"));
			return new BigInteger(1, bytes).toString(16);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("md5加密错误");
		}
	}

}