package com.xwj.social.qq.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

import com.xwj.social.qq.api.QQ;

/**
 * 连接工厂，用来创建Connect
 * 
 * @author xwj
 *
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

	/**
	 * @param providerId
	 *            服务商id
	 */
	public QQConnectionFactory(String providerId, String appId, String appSecret) {
		/**
		 * serviceProvider 用于执行授权流和获取本机服务API实例的ServiceProvider模型 
		 * apiAdapter适配器，用于将不同服务提供商的个性化用户信息映射到 Connection
		 */
		super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
	}

}
