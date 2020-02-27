package com.xwj.utils;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 签名工具类
 */
@Slf4j
public class SignUtil {

	private static final String ALGORITHM = "MD5withRSA";

	/**
	 * 私钥对原始数据进行签名
	 */
	@SneakyThrows
	public static String sign(String data, PrivateKey privateKey) {
		Signature signature = Signature.getInstance(ALGORITHM);
		signature.initSign(privateKey);
		signature.update(data.getBytes());
		return new String(Base64Util.encode(signature.sign()));
	}

	/**
	 * 公钥、原始数据、原始签名数据进行验证
	 */
	@SneakyThrows
	public static boolean verify(String publicKey, String data, String sign) {
		PublicKey key = RSAUtil.getPublicKey(publicKey);
		Signature signature = Signature.getInstance(ALGORITHM);
		signature.initVerify(key);
		signature.update(data.getBytes());
		return signature.verify(Base64Util.decode(sign.getBytes()));
	}

	/**
	 * 校验签名
	 */
	public static boolean checkSignature(String signature, String token, String timestamp, String nonce) {
		if ((signature == null) || (timestamp == null) || (nonce == null)) {
			return false;
		}
		String local = getSignature(token, timestamp, nonce);
		return local != null ? local.equals(signature.toUpperCase(Locale.ENGLISH)) : false;
	}

	/**
	 * 获取签名
	 */
	public static String getSignature(String token, String timestamp, String nonce) {
		String content = StringUtils.join(token, timestamp, nonce);
		String ciphertext = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.toString().getBytes());
			ciphertext = byteToStr(digest);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return ciphertext;
	}

	/**
	 * byte数组转为字符串
	 */
	private static String byteToStr(byte[] byteArray) {
		StringBuffer sbuffer = new StringBuffer();
		for (int i = 0, len = byteArray.length; i < len; i++) {
			sbuffer.append(byteToHexStr(byteArray[i]));
		}
		return sbuffer.toString();
	}

	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4 & 0xF)];
		tempArr[1] = Digit[(mByte & 0xF)];

		String s = new String(tempArr);
		return s;
	}

}
