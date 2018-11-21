package com.xwj.social.qq.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

import com.xwj.social.qq.api.QQ;
import com.xwj.social.qq.api.QQUserInfo;

/**
 * 适配器，用于将不同服务提供商的个性化用户信息映射到Connection
 */
public class QQAdapter implements ApiAdapter<QQ> {

	/**
	 * 测试QQ服务是否可用
	 */
	@Override
	public boolean test(QQ api) {
		return true;
	}

	/**
	 * Connection和Api之间做适配
	 */
	@Override
	public void setConnectionValues(QQ api, ConnectionValues values) {
		QQUserInfo userInfo = api.getUserInfo();
		values.setDisplayName(userInfo.getNickname());
		values.setImageUrl(userInfo.getFigureurl_qq_1());
		values.setProfileUrl(null); // 主页地址。QQ没有，微博有
		values.setProviderUserId(userInfo.getOperId());// 服务提供商返回的该user的openId
	}

	@Override
	public UserProfile fetchUserProfile(QQ api) {
		return null;
	}

	@Override
	public void updateStatus(QQ api, String message) {

	}

}
