package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("index")
public class IndexController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/rest")
	public String rest() {
		String url = "http://localhost:18087/config/test";
		return restTemplate.getForObject(url, String.class);
	}

}
