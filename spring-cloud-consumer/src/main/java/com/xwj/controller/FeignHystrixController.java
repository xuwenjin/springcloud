package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.entity.UserEntity;
import com.xwj.feign.UserFeignClient;

@RestController
public class FeignHystrixController {
	
	@Autowired
	private UserFeignClient client;
	
	@GetMapping("/get/{id}")
	public UserEntity getOne(@PathVariable Long id) {
		UserEntity user = client.findById(id);
		return user;
	}

}
