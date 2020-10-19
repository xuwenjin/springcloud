package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.entity.UserInfoVo;
import com.xwj.feign.UserFeignClient;

@RestController
public class FeignController {

	@Autowired
	private UserFeignClient feignClient;

	@GetMapping("/find/{id}")
	public UserInfoVo getOne(@PathVariable Long id) {
		UserInfoVo user = feignClient.findById(id);
		return user;
	}

	@GetMapping("/saveUser")
	public UserInfoVo saveUser() {
		UserInfoVo user = new UserInfoVo();
		user.setAge(10);
		user.setUsername("xuwenjin");
		return feignClient.saveUser(user);
	}

}
