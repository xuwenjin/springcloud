package com.xwj.feign;

import org.springframework.stereotype.Component;

import com.xwj.entity.UserInfoVo;

/**
 * 降级回调类
 */
@Component
public class UserFallback implements UserFeignClient {

	@Override
	public UserInfoVo findById(Long id) {
		UserInfoVo user = new UserInfoVo();
		user.setId("1000");
		user.setAge(12);
		return user;
	}

	@Override
	public UserInfoVo saveUser(UserInfoVo user) {
		return null;
	}

}
