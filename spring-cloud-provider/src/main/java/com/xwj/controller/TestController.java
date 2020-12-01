package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.billcode.SequenceGenerator;
import com.xwj.properties.TestConfiguration;
import com.xwj.service.test.IBaseService;

@RestController
@RequestMapping("test")
public class TestController {

	@Autowired(required = false)
	private TestConfiguration test1Configuration;
	@Autowired
	private IBaseService baseService;

	@GetMapping("testConfiguration")
	public String testConfiguration() {
		if (test1Configuration != null) {
			return test1Configuration.getField();
		}
		return "OK";
	}

	/**
	 * 雪花算法-测试生成id(QPS：1200左右)
	 * @return
	 */
	@GetMapping("idWorker")
	public String idWorker() {
		return SequenceGenerator.uuid16();
	}

	/**
	 * 测试@Conditional相关注解
	 */
	@GetMapping("testConditional")
	public String testConditional() {
		return baseService.sayHello();
	}

}
