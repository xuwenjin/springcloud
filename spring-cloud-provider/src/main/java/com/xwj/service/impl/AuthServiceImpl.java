package com.xwj.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.auth.CacheService;
import com.xwj.entity.UserInfo;
import com.xwj.exception.AuthException;
import com.xwj.jwt.JwtTokenService;
import com.xwj.jwt.JwtUserInfo;
import com.xwj.service.AuthService;
import com.xwj.service.IUserService;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private JwtTokenService jwtTokenUtil;
	@Autowired
	private IUserService userService;
	@Autowired
	private CacheService cacheService;

	/**
	 * 登录
	 */
	@Override
	public String login(String username, String password) {
		// 校验账户密码
		UserInfo user = userService.validate(username, password);
		if (user == null) {
			throw new AuthException("用户不存在或账户密码错误!");
		}

		// 生成token
		JwtUserInfo userInfo = new JwtUserInfo(user.getUsername(), user.getId() + "", user.getName());
		String token = jwtTokenUtil.generateToken(userInfo);

		// 将token放入redis
		cacheService.setJwtToken(username, token);

		return token;
	}

	@Override
	public String refresh(String oldToken) {
		JwtUserInfo userInfo;
		try {
			userInfo = (JwtUserInfo) jwtTokenUtil.getInfoFromToken(oldToken);
		} catch (ExpiredJwtException e) {
			throw new AuthException("token已过期");
		} catch (Exception e) {
			throw new AuthException("无效的token");
		}
		if (userInfo == null || StringUtils.isEmpty(userInfo.getUniqueName())) {
			throw new AuthException("无效的token");
		}
		String token = jwtTokenUtil.generateToken(userInfo);

		// 将token放入redis
		cacheService.setJwtToken(userInfo.getUniqueName(), token);

		return token;
	}

}
