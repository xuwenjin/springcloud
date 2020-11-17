package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.xwj.entity.UserInfoVo;

/**
 * 测试使用ribbon做负载均衡
 */
@RestController
@RequestMapping("myribbon")
public class RibbonController {

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/getOne/{id}")
	public UserInfoVo getOne(@PathVariable Long id) {
		UserInfoVo user = restTemplate.getForObject("http://service-provider/user/find/" + id, UserInfoVo.class);
		return user;
	}

}
