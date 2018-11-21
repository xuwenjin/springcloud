package com.xwj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import com.xwj.entity.MyUser;

import lombok.extern.slf4j.Slf4j;

/**
 * UserDetailsService用于返回用户相关数据
 */
@Slf4j
@Service
public class MyUserDetailServiceImpl implements UserDetailsService, SocialUserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 根据username查询用户实体
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("登录用户名:{}", username);

		// 密码加密(这里是将密码写死的，真实情况应该是查询数据库)
		String dbPassword = passwordEncoder.encode("1234");
		log.info("数据库密码{}", dbPassword);
		return new MyUser(username, dbPassword);
	}

	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		log.info("登录用户名:{}", userId);
		
		// 密码加密(这里是将密码写死的，真实情况应该是查询数据库)
		String dbPassword = passwordEncoder.encode("1234");
		log.info("数据库密码{}", dbPassword);
		return new SocialUser(userId, dbPassword, true, true, true, true,
				AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

}
