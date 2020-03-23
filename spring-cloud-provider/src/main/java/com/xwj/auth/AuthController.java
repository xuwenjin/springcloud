package com.xwj.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.xwj.annotations.IgnoreAuth;
import com.xwj.annotations.IgnoreEncode;
import com.xwj.annotations.ReplayLimit;
import com.xwj.annotations.RequestLimit;
import com.xwj.common.AuthConsts;
import com.xwj.common.RsaKey;
import com.xwj.entity.UserInfo;
import com.xwj.utils.AESUtil;
import com.xwj.utils.CommonUtil;
import com.xwj.utils.MD5Util;
import com.xwj.utils.RSAUtil;
import com.xwj.utils.SignUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试权限认证
 */
@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

	@Autowired
	private LoginInfoService loginInfoService;
	@Autowired
	private CacheService cacheService;

	private static final AtomicInteger count1 = new AtomicInteger();
	private static final AtomicInteger count2 = new AtomicInteger();

	/**
	 * 登录(免鉴权)
	 * 
	 * @return 返回accessToken和用户token
	 */
	@IgnoreAuth
	@GetMapping("/login")
	public String login(String phone) {
		String accessToken = UUID.randomUUID().toString();

		Map<String, String> account = new HashMap<>();
		account.put("accessToken", accessToken);
		account.put("mobile", phone);
		account.put("token", "123321"); // 请求车联网返回的用户token

		// 设置登录信息
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

		Map<String, Object> map = new HashMap<>();
		map.put("name", "xuwenjin");
		map.put("age", 10);
		String json = JSON.toJSONString(map);

		// User vo = new User();
		// vo.setId(1L);
		// vo.setLastName("张三");
		// json = JSON.toJSONString(vo);

		String aesKey = CommonUtil.generateKey();
		RsaKey rsaKey = AuthUtil.rsaKeyMap.get(AuthConsts.DEFAULT_APPID);
		String key = RSAUtil.publicEncrypt(aesKey, rsaKey.getRequestPublicKey());
		String data = AESUtil.encrypt(json, aesKey);
		System.out.println("data: " + data);
		System.out.println("key: " + key);
		try {
			System.out.println("key2: " + URLEncoder.encode(key, "UTF-8"));
			System.out.println("data2: " + URLEncoder.encode(data, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return "OK";
	}

	/**
	 * 测试基础鉴权(get请求)
	 */
	@GetMapping("/testBaseGet")
	public String testBaseGet(String name, int age) {
		System.out.println("name:" + name + ", age:" + age);
		return "testBaseGet";
	}

	/**
	 * 测试基础鉴权(get请求，请求参数拼在url后面)
	 */
	@GetMapping("/testBaseGet2/{name}")
	public String testBaseGet2(@PathVariable String name) {
		System.out.println("name:" + name);
		return "testBaseGet2";
	}

	/**
	 * 测试基础鉴权(post请求)
	 */
	@PostMapping("/testBasePost")
	public String testBasePost(String name, int age) {
		System.out.println("name:" + name + ", age:" + age);
		return "testBasePost";
	}

	/**
	 * 测试基础鉴权(post请求)
	 */
	@PostMapping("/testBasePost2")
	public String testBasePost2(@RequestBody UserInfo vo) {
		log.info("id:{}, name:{}", vo.getId(), vo.getUsername());
		return "testBasePost2";
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
	@RequestLimit(period = 10, count = 3)
	@GetMapping("/testRequestLimit")
	public int testRequestLimit(String name) {
		return count1.incrementAndGet();
	}

	/**
	 * 限流2
	 */
	@IgnoreEncode
	@RequestLimit(period = 10, count = 10)
	@GetMapping("/testRequestLimit2")
	public int testRequestLimit2() {
		return count2.incrementAndGet();
	}

	/**
	 * 添加黑名单
	 */
	@IgnoreEncode
	@IgnoreAuth
	@PostMapping("/addBlackIp")
	public void addBlackIp(String blackIp) {
		cacheService.addBlackIp(blackIp);
	}

	/**
	 * 解除黑名单
	 */
	@IgnoreEncode
	@IgnoreAuth
	@PostMapping("/removeBlackIp")
	public void removeBlackIp(String blackIp) {
		cacheService.removeBlackIp(blackIp);
	}

}