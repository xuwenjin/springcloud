package com.xwj.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.xwj.annotations.IgnoreAuth;
import com.xwj.annotations.IgnoreEncode;
import com.xwj.common.AuthConsts;
import com.xwj.common.RsaKey;
import com.xwj.service.AuthService;
import com.xwj.utils.AESUtil;
import com.xwj.utils.CommonUtil;
import com.xwj.utils.MD5Util;
import com.xwj.utils.RSAUtil;
import com.xwj.utils.SignUtil;

/**
 * 测试权限认证(使用jwt)
 */
@RestController
@RequestMapping("jwt")
public class JwtAuthController {

	@Autowired
	private AuthService authService;

	/**
	 * 获取token
	 */
	@IgnoreAuth
	@IgnoreEncode
	@GetMapping("/getToken")
	public String getToken(String username, String password) {
		return authService.login(username, password);
	}

	/**
	 * 刷新token
	 */
	@IgnoreAuth
	@IgnoreEncode
	@GetMapping("/refreshToken")
	public String refreshToken(String token) {
		return authService.refresh(token);
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

}