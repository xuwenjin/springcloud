package com.xwj.social.qq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

import com.xwj.properties.QQProperty;
import com.xwj.properties.SecurityProperty;
import com.xwj.social.qq.connect.QQConnectionFactory;

@Configuration
// 当配置了app-id的时候才启用
@ConditionalOnProperty(prefix = "security.social.qq", name = "appId")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperty securityProperty;

	@Override
	public ConnectionFactory<?> createConnectionFactory() {
		QQProperty qq = securityProperty.getSocial().getQq();
		return new QQConnectionFactory(qq.getProviderId(), qq.getAppId(), qq.getAppSecret());
	}

}
