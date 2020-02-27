package com.xwj.utils;

import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import lombok.SneakyThrows;

public class AESUtil {

	public static final String ALGORITHM = "AES";

	public static String binary(byte[] bytes, int radix) {
		return new BigInteger(1, bytes).toString(radix);
	}

	/**
	 * AES加密
	 * 
	 * @param key
	 *            秘钥
	 * @param data
	 *            被加密的数据
	 */
	@SneakyThrows
	public static String encode(String key, String data) {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		keyGenerator.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), ALGORITHM));
		byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64Util.encode(bytes);
	}

	/**
	 * AES解密
	 * 
	 * @param key
	 *            秘钥
	 * @param data
	 *            加密后的字符串
	 */
	@SneakyThrows
	public static String decode(String key, String data) {
		if (StringUtils.isEmpty(data)) {
			return null;
		}
		byte[] bytes = Base64Util.decode(data);
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
		keyGenerator.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), ALGORITHM));
		byte[] decryptBytes = cipher.doFinal(bytes);
		return new String(decryptBytes);
	}

}