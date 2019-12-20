package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.factory.FuncFactory;
import com.xwj.service.OpenApiService;

@RestController
@RequestMapping("my")
public class MyController {

	@Autowired
	private OpenApiService openApiService;
	@Autowired
	private FuncFactory funcFactory;

	@RequestMapping("/test/openapi")
	public void testOpenApi() {
		// openApiService.getOpenApi();
		openApiService.getOpenApi2();
		// openApiService.postOpenApi();
	}

	@RequestMapping("/test/factory")
	public void testFactory() {
		funcFactory.getQueryFunc("002").print();
		funcFactory.getQueryFunc2("002").print();
	}

}
