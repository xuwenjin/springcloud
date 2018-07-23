package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.entity.UserEntity;
import com.xwj.feign.UserFeignClient;

@RestController
public class FeignController {

	@Autowired
	private UserFeignClient feignClient;

	@GetMapping("/find/{id}")
	public UserEntity getOne(@PathVariable Long id) {
		UserEntity user = feignClient.findById(id);
		return user;
	}
	
	@GetMapping("/getUser")
	public UserEntity findUser() {
		UserEntity user = new UserEntity();
		user.setId("100");
		user.setAge(10);
		user.setLastName("xuwenjin");
		return feignClient.findUser(user);
	}

}
