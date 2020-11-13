package com.xwj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试nacos服务发现
 * 
 * @author xwj
 */
@RestController
public class IndexController {

	@Value("${server.port}")
	private String port;

	@GetMapping("/hello")
	public String hello(@RequestParam String name) {
		System.out.println("port:" + port);
		return "hello " + name;
	}

}
