package com.xwj.advice;

import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xwj.auth.AuthUtil;
import com.xwj.common.ApiResponseData;
import com.xwj.common.RsaKey;
import com.xwj.common.SecurityResponse;
import com.xwj.utils.AESUtil;
import com.xwj.utils.CommonUtil;
import com.xwj.utils.RSAUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 加密数据
 */
@Slf4j
public class EncryptHttpOutputMessage {

	public static Object getResponse(Object obj, ServerHttpResponse httpResponse, String appId) {
		String json = null;
		try {
			if (obj instanceof String) {
				json = (String) obj;
				httpResponse.getHeaders()
						.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
			} else {
				// 默认是不输出值为null的字段，这里设置为输出
				json = toJsonStringWithNullValue(obj);
			}
			// 如果出现异常，则直接输出
			if (obj instanceof ApiResponseData) {
				ApiResponseData response = (ApiResponseData) obj;
				if (!response.isSuccess()) {
					return obj;
				}
			}
			// 如果出现异常，则直接输出
			if (json.contains("error") || json.contains("exception")) {
				return obj;
			}
			log.info("返回参数加密前：{}", json);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "后台异常";
		}
		// 1、生成AES秘钥
		String key = CommonUtil.generateKey();
		// 2、AES用秘钥加密
		String data = AESUtil.encrypt(json, key);
		try {
			// 3、使用服务端RSA公钥对AES秘钥加密
			RsaKey rsaKey = AuthUtil.rsaKeyMap.get(appId);
			key = RSAUtil.publicEncrypt(key, rsaKey.getResponsePublicKey());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		SecurityResponse response = new SecurityResponse();
		response.setKey(key);
		response.setData(data);
		if (obj instanceof String) {
			return toJsonStringWithNullValue(response);
		}
		return response;
	}

	/**
	 * Object转json字符串
	 * 
	 * 默认是不输出值为null的字段，这里设置为输出
	 */
	private static String toJsonStringWithNullValue(Object obj) {
		return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
	}

}
