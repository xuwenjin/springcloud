package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.xwj.entity.UserEntity;

@RestController
public class IndexController {

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/find/{id}")
	public UserEntity findById(@PathVariable Long id) {
		return restTemplate.getForObject("http://service-provider/find/" + id, UserEntity.class);
	}

}
