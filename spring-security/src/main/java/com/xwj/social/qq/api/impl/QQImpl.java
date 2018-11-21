package com.xwj.social.qq.api.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xwj.social.qq.api.QQ;
import com.xwj.social.qq.api.QQUserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

	private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

	private static final String URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

	/**
	 * 分配给应用的appid
	 */
	private String appId;
	/**
	 * 用户的ID，与QQ号码一一对应。
	 */
	private String openId;

	private ObjectMapper objectMapper = new ObjectMapper();

	public QQImpl(String accessToken, String appId) {
		// 默认是将accessToken放入请求头中，现在改为放入请求参数中
		super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
		this.appId = appId;

		// 通过accessToken获取openId
		String url = String.format(URL_GET_OPENID, accessToken);
		String result = getRestTemplate().getForObject(url, String.class);
		log.info("【QQImpl】 URL_GET_OPENID={} result={}", URL_GET_OPENID, result);

		this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
	}

	@Override
	public QQUserInfo getUserInfo() {
		// 通过appId和openId获取用户信息
		String url = String.format(URL_GET_USER_INFO, appId, openId);
		String result = getRestTemplate().getForObject(url, String.class);
		log.info("【QQImpl】 URL_GET_USER_INFO={} result={}", URL_GET_USER_INFO, result);

		QQUserInfo userInfo = null;
		try {
			userInfo = objectMapper.readValue(result, QQUserInfo.class);
			// userInfo.setOpenId(openId);
			return userInfo;
		} catch (Exception e) {
			throw new RuntimeException("获取用户信息失败", e);
		}
	}

}
