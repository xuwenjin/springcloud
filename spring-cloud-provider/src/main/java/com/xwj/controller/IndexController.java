package com.xwj.controller;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

//	@Resource(name = "jasyptStringEncryptor")
	@Autowired
	private StringEncryptor encryptor;

	@Value("${spring.datasource.password}")
	private String dbPassword; // 数据库密码

	/**
	 * 测试jasypt加密解密
	 */
	@GetMapping("/jasypt/{password}")
	public String testJasypt(@PathVariable String password) {
		String encryptPwd = encryptor.encrypt(password);
		System.out.println("加密：" + encryptPwd);
		System.out.println("解密：" + encryptor.decrypt(encryptPwd));
		return encryptPwd;
	}

	/**
	 * 测试配置文件字段加密后，项目中该字段的值
	 */
	@GetMapping("/password")
	public String password() {
		return dbPassword;
	}

}