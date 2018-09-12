package com.xwj.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

	/**
	 * 需要认证的
	 */
	@RequestMapping("/user")
	public String user() {
		return "user";
	}
	
	/**
	 * 不需要认证的
	 */
	@RequestMapping("/formLogin")
	public String login() {  
		return "formLogin";
	}
	
//	@RequestMapping("/me")
//	@ResponseBody
//	public Object authentication(Authentication authentication) {
//		return authentication;
//	}
	
	@RequestMapping("/home")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/plogin")
	public String plogin() {
		return "plogin";
	}

}