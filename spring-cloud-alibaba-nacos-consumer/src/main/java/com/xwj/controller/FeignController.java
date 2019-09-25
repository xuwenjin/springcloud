package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.feign.MyFeignClient;

@RestController
public class FeignController {

	@Autowired
	private MyFeignClient feignClient;

	@GetMapping("/test")
	public String test() {
		String result = feignClient.hello("小红帽");
		return "Return : " + result;
	}

}
