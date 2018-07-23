package com.xwj.feign;

import org.springframework.stereotype.Component;

import com.xwj.entity.UserEntity;

/**
 * 错误回调类
 */
@Component
public class UserFallback implements UserFeignClient {

	@Override
	public UserEntity findById(Long id) {
		UserEntity user = new UserEntity();
		user.setId("1000");
		user.setAge(12);
		return user;
	}

}
