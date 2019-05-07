package com.xwj.controller;

import javax.annotation.Resource;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xwj.operlog.MyLog;

@Controller
public class IndexController {

	@Resource(name = "jasyptStringEncryptor")
	private StringEncryptor encryptor;
	
	@Value("${spring.datasource.password}")
	private String password;

	@MyLog("登录")
	@GetMapping("/login")
	public void login() {
	}

	/**
	 * 测试加密
	 */
	@ResponseBody
	@GetMapping("/encrypt/{str}")
	public String testEncrypt(@PathVariable String str) {
		return encryptor.encrypt(str);
	}

	/**
	 * 测试解密
	 * 
	 * @param encryptStr 加密后的字符串
	 */
	@ResponseBody
	@GetMapping("/decrypt/{encryptStr}")
	public String testDecrypt(@PathVariable String encryptStr) {
		return encryptor.decrypt(encryptStr);
	}
	
	@ResponseBody
	@GetMapping("/password")
	public String password() {
		return password;
	}

}