package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.OpenApiService;

@RestController
@RequestMapping("my")
public class MyController {
	
	@Autowired
	private OpenApiService openApiService;
	
	@RequestMapping("/test/openapi")
	public void testOpenApi() {
//		openApiService.getOpenApi();
		openApiService.getOpenApi2();
//		openApiService.postOpenApi();
	}

}
