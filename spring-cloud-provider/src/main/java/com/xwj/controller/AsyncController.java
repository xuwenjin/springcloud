package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.AsyncService;

import lombok.extern.slf4j.Slf4j;

/**
 * 异步测试
 * 
 * 
 * 如下方式会使@Async失效：
 * 
 * 1、异步方法使用static修饰
 * 
 * 2、异步类没有使用@Component注解（或其他注解）导致spring无法扫描到异步类
 * 
 * 3、异步方法不能与被调用的异步方法在同一个类中
 * 
 * 4、类中需要使用@Autowired或@Resource等注解自动注入，不能自己手动new对象
 * 
 * 5、如果使用SpringBoot框架必须在启动类中增加@EnableAsync注解
 * 
 */
@Slf4j
@RestController
@RequestMapping("async")
public class AsyncController {

	@Autowired
	private AsyncService asyncService;

	@GetMapping("sendMessage")
	public String sendMessage() {
		asyncService.sendMessage1();
		asyncService.sendMessage2();
		log.info("OK");
		return "OK";
	}

}
