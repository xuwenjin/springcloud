package com.xwj.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.xwj.entity.MyUser;

/**
 * UserDetailsService用于返回用户相关数据
 * 
 * @author xwj
 *
 */
@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

	/**
	 * 根据username查询用户实体
	 */
	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		System.out.println("进来了。。。");
		MyUser user = new MyUser("xwj", "1234");
		return user;
	}

}
