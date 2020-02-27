package com.xwj.advice;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xwj.common.RsaKey;
import com.xwj.common.SecurityRequest;
import com.xwj.interceptor.AuthUtil;
import com.xwj.utils.AESUtil;
import com.xwj.utils.RSAUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 解密数据
 */
@Slf4j
public class DecryptHttpInputMessage implements HttpInputMessage {

	private HttpHeaders headers;
	private InputStream body;

	public DecryptHttpInputMessage(HttpInputMessage inputMessage, String appId) throws Exception {
		String json = IOUtils.toString(inputMessage.getBody(), "UTF-8");
		try {
			SecurityRequest request = JSON.parseObject(json, SecurityRequest.class);
			String key = request.getKey();
			String data = request.getData();
			if (!StringUtils.isBlank(key) && !StringUtils.isBlank(data)) {
				// 通过appId获取RSA秘钥
				RsaKey rsaKey = AuthUtil.rsaKeyMap.get(appId);
				// 通过RSA私钥解密，得到AES的秘钥
				String aesKey = RSAUtil.privateDecrypt(key, rsaKey.getRequestPrivateKey());
				// 通过AES秘钥，得到真实的请求数据
				json = AESUtil.decode(aesKey, data);
				JSONObject jsonObject = JSONObject.parseObject(json);

				// 将请求头里面的几个字段添加到请求体里面
				String accessToken = inputMessage.getHeaders().getFirst("AccessToken");
				String timestamp = inputMessage.getHeaders().getFirst("Timestamp");// 页面传的参数
				String nonce = inputMessage.getHeaders().getFirst("Nonce");// 页面传的参数,随机数,用来防止重放
				String signature = inputMessage.getHeaders().getFirst("Signature");// 页面传的参数

				Object ov = jsonObject.get("appId");
				if (ov == null) {
					jsonObject.put("appId", appId);// appId
				}
				ov = jsonObject.get("key");
				if (ov == null) {
					jsonObject.put("key", aesKey);// AES秘钥
				}
				ov = jsonObject.get("nonce");
				if (ov == null) {
					jsonObject.put("nonce", nonce);// 随机字符串
				}
				ov = jsonObject.get("timestamp");
				if (ov == null) {
					jsonObject.put("timestamp", timestamp);// 时间戳
				}
				ov = jsonObject.get("accessToken");
				if (ov == null) {
					jsonObject.put("accessToken", accessToken);// 口令
				}
				ov = jsonObject.get("signature");
				if (ov == null) {
					jsonObject.put("signature", signature);// 时间戳
				}
				json = jsonObject.toJSONString();
				log.info("app请求解密:{}", json);
			} else {
				log.error("app请求未加密参数:{}", json);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		this.headers = inputMessage.getHeaders();
		this.body = IOUtils.toInputStream(json, "UTF-8");
	}

	@Override
	public InputStream getBody() throws IOException {
		return body;
	}

	@Override
	public HttpHeaders getHeaders() {
		return headers;
	}

}
