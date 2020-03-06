package com.xwj.advice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xwj.auth.AuthUtil;
import com.xwj.common.RsaKey;
import com.xwj.common.SecurityRequest;
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
			log.info("key:{}", key);
			log.info("data:{}", data);
			if (!StringUtils.isBlank(key) && !StringUtils.isBlank(data)) {
				// 通过appId获取RSA秘钥
				RsaKey rsaKey = AuthUtil.rsaKeyMap.get(appId);
				// 通过RSA私钥解密，得到AES的秘钥
				String aesKey = RSAUtil.privateDecrypt(key, rsaKey.getRequestPrivateKey());
				// 通过AES秘钥，得到真实的请求数据
				json = AESUtil.decrypt(data, aesKey);
				JSONObject jsonObject = JSONObject.parseObject(json);

				// 将请求头里面的几个字段添加到请求体里面(这样在后面controller中，就可以从请求参数中得到请求头的数据)
				String accessToken = inputMessage.getHeaders().getFirst("AccessToken");
				String timestamp = inputMessage.getHeaders().getFirst("Timestamp");// 页面传的参数
				String nonce = inputMessage.getHeaders().getFirst("Nonce");// 页面传的参数,随机数,用来防止重放
				String signature = inputMessage.getHeaders().getFirst("Signature");// 页面传的参数
				this.setJsonValueIfNull(jsonObject, "appId", appId);
				this.setJsonValueIfNull(jsonObject, "key", aesKey);
				this.setJsonValueIfNull(jsonObject, "nonce", nonce);
				this.setJsonValueIfNull(jsonObject, "timestamp", timestamp);
				this.setJsonValueIfNull(jsonObject, "accessToken", accessToken);
				this.setJsonValueIfNull(jsonObject, "signature", signature);

				json = jsonObject.toJSONString();
				log.info("app请求解密:{}", json);
			} else {
				// 如果key和data解析不出来，就直接把数据丢到controller
				log.info("app请求未加密，参数:{}", json);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		this.headers = inputMessage.getHeaders();
		this.body = IOUtils.toInputStream(json, "UTF-8");
	}

	/**
	 * 设置JSON的值
	 */
	private void setJsonValueIfNull(JSONObject jsonObject, String sKey, String sValue) {
		Object obj = jsonObject.get(sKey);
		if (Objects.isNull(obj)) {
			jsonObject.put(sKey, sValue);
		}
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
