package com.xwj.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import lombok.SneakyThrows;

/**
 * AES加解密工具类
 */
public class AESUtil {

	private static final String ALGORITHM = "AES";

	/**
	 * AES加密
	 * 
	 * @param key  秘钥
	 * @param data 被加密的数据
	 */
	@SneakyThrows
	public static String encrypt(String data, String key) {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), ALGORITHM));
		byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64Util.encode(bytes);
	}

	/**
	 * AES解密
	 * 
	 * @param data 加密后的字符串
	 * @param key  秘钥
	 */
	@SneakyThrows
	public static String decrypt(String data, String key) {
		if (StringUtils.isEmpty(data)) {
			return null;
		}
		byte[] bytes = Base64Util.decode(data);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), ALGORITHM));
		byte[] decryptBytes = cipher.doFinal(bytes);
		return new String(decryptBytes);
	}

	public static void main(String[] args) {
		String srcData = "12412312312312";
		String key = CommonUtil.generateKey();
		String encodeStr = encrypt(srcData, key);
		System.out.println("加密后字符串：" + encodeStr);
		System.out.println("解密后字符串：" + decrypt(encodeStr, key));
	}

}