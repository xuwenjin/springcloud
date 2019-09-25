package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.RetryService;

import lombok.extern.slf4j.Slf4j;

/**
 * 重试
 * 
 * @author xwj
 */
@Slf4j
@RestController
@RequestMapping("retry")
public class RetryController {
	
	@Autowired
	private RetryService retryService;
	
	@GetMapping("test")
	public String testRetry() {
		log.info("开始");
		String hello = retryService.hello();
		log.info("结束");
		return hello;
//		return retryService.hello2();
	}
	
	@GetMapping("test2")
	public String guavaRetry() {
		return retryService.guavaRetry();
	}

}
