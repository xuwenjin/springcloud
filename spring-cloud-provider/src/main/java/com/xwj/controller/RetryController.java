package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.RetryService;

/**
 * 重试
 * 
 * @author xwj
 */
@RestController
@RequestMapping("retry")
public class RetryController {
	
	@Autowired
	private RetryService retryService;
	
	@GetMapping("test")
	public String testRetry() {
//		return retryService.hello();
		return retryService.hello2();
	}
	
	@GetMapping("test2")
	public String guavaRetry() {
		return retryService.guavaRetry();
	}

}
