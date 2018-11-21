package com.xwj.social;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import com.xwj.properties.SecurityProperty;
import com.xwj.social.qq.config.OAuthSpringSocialConfigurer;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SecurityProperty securityProperty;

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		/**
		 * textEncryptor： 存到数据库时的加密方式。Encryptors.noOpText()表示不加密
		 */
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator, Encryptors.noOpText());
		repository.setTablePrefix("xwj_"); // 设置表名前缀
		return repository;
	}

	/**
	 * 将SocialAuthenticationFilter放到Security的过滤器链中
	 */
	@Bean
	public SpringSocialConfigurer socialSecurityConfig() {
		String filterProcessesUrl = securityProperty.getSocial().getFilterProcessesUrl();
		OAuthSpringSocialConfigurer config = new OAuthSpringSocialConfigurer(filterProcessesUrl);
		return config;
	}

}
