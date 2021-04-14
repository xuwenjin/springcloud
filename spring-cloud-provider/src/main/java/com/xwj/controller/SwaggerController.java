package com.xwj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.entity.UserInfo;

import io.swagger.annotations.ApiOperation;

/**
 * 测试Swagger
 * 
 * 访问swagger-ui：http://localhost:18081/swagger-ui.html
 * 
 * @author xuwenjin 2021年4月14日
 */
@RestController
@RequestMapping("sw")
@ApiOperation("测试swagger")
public class SwaggerController {

	@ApiOperation("测试swagger的hello接口")
	@GetMapping("/hello")
	public String hello() {
		return "hello world";
	}

	@ApiOperation("测试swagger的save接口")
	@PostMapping("save")
	public UserInfo save(@RequestBody UserInfo user) {
		return new UserInfo("xwj", "123456");
	}

}
