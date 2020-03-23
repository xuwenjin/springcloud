package com.xwj.jwt;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xwj.common.CommonConsts;
import com.xwj.properties.KeyConfiguration;
import com.xwj.utils.JwtUtil;

import io.jsonwebtoken.Claims;

/**
 * jwt服务
 */
@Component
public class JwtTokenService {

	@Value("${jwt.expire:3600}")
	private int expire; // token失效时间(秒)
	@Value("${jwt.secret:}")
	private String secret; // jwt秘钥
	@Autowired
	private KeyConfiguration keyConfiguration;

	/**
	 * 获取token
	 */
	public String generateToken(IJwtInfo jwtInfo) {
		return JwtUtil.generateTokenByPriKey(jwtInfo, keyConfiguration.getUserPriKey(), expire);
		// return JwtUtil.generateToken(jwtInfo, secret, expire);
	}

	/**
	 * 获取用户信息
	 */
	public IJwtInfo getInfoFromToken(String token) throws Exception {
		Claims claims = JwtUtil.parserTokenByPubKey(token, keyConfiguration.getUserPubKey());
		// Claims claims = JwtUtil.parserToken(token, secret);
		String userId = Objects.toString(claims.get(CommonConsts.JWT_KEY_USER_ID), null);
		String name = Objects.toString(claims.get(CommonConsts.JWT_KEY_NAME), null);
		return new JwtUserInfo(claims.getSubject(), userId, name);
	}

}