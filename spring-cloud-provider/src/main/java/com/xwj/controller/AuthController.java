package com.xwj.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.annotations.IgnoreAuth;
import com.xwj.annotations.IgnoreEncode;
import com.xwj.annotations.ReplayLimit;
import com.xwj.annotations.RequestLimit;
import com.xwj.cache.LoginInfoService;
import com.xwj.enums.LimitType;
import com.xwj.utils.MD5Util;
import com.xwj.utils.SignUtil;

/**
 * 测试权限认证
 */
@RestController
@RequestMapping("auth")
public class AuthController {

	@Autowired
	private LoginInfoService loginInfoService;

	private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

	/**
	 * 登录。返回accessToken
	 */
	@IgnoreAuth
	@GetMapping("/login")
	public String login() {
		String accessToken = UUID.randomUUID().toString();
		String phone = "18207136675";

		Map<String, String> account = new HashMap<>();
		account.put("accessToken", accessToken);
		account.put("phone", phone);
		account.put("token", "123321");

		loginInfoService.setLoginInfo(accessToken, account);
		loginInfoService.setLastLoginInfo(phone, account);

		return accessToken;
	}

	/**
	 * 计算鉴权所需要的的值(调试用)
	 */
	@IgnoreAuth
	@IgnoreEncode
	@GetMapping("/calc")
	public String calcParam() {
		long timestamp = System.currentTimeMillis();
		String timestampStr = String.valueOf(timestamp);
		System.out.println("当前时间戳：" + timestampStr);

		String nonce = "123456";
		String token = "123321";
		String sign = SignUtil.getSignature(token, timestampStr, nonce);
		System.out.println("签名：" + sign);

		String hs = MD5Util.md5(timestamp + "_" + nonce);
		System.out.println("hs：" + hs);

		return "OK";
	}

	/**
	 * 忽略鉴权
	 */
	@IgnoreAuth
	@IgnoreEncode
	@GetMapping("/testIgnoreAuth")
	public String testIgnoreAuth() {
		return "testIgnoreAuth";
	}

	/**
	 * 测试基础鉴权
	 */
	@GetMapping("/testBase")
	public String testBase(String name, int age) {
		return "testBase";
	}

	/**
	 * 防重放
	 */
	@ReplayLimit
	@GetMapping("/testReplayLimit")
	public String testReplayLimit() {
		return "testReplayLimit";
	}

	/**
	 * 限流
	 */
	@IgnoreEncode
	@RequestLimit(key = "test", period = 10, count = 3, prefix = "limit", limitType = LimitType.IP)
	@GetMapping("/testRequestLimit")
	public int testRequestLimit() {
		// 意味着10秒内最多可以访问3次
		return ATOMIC_INTEGER.incrementAndGet();
	}

}