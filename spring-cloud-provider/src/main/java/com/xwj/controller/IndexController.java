package com.xwj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.xwj.operlog.MyLog;

@Controller
public class IndexController {
	
	@MyLog("登录")
	@GetMapping("/login")
	public void login(){
	}

}