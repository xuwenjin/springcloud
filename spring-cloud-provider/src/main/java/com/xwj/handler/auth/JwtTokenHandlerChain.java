package com.xwj.handler.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.xwj.auth.CacheService;
import com.xwj.common.ApiResponseData;
import com.xwj.handler.AbstractHandler;
import com.xwj.handler.HandlerChain;
import com.xwj.jwt.JwtTokenService;
import com.xwj.jwt.JwtUserInfo;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenHandlerChain extends AbstractHandler {

	@Autowired
	private JwtTokenService jwtTokenService;
	@Autowired
	private CacheService cacheService;

	@Override
	public ApiResponseData handleAuth(HandlerChain chain, HandlerMethod handlerMethod, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("token验证");
		try {
			// 1、校验是否缺少参数accessToken
			String token = request.getHeader("AccessToken");
			log.info("accessToken:{}", token);
			if (StringUtils.isEmpty(token)) {
				return ApiResponseData.unauthorizedError("AccessToken不能为空");
			}

			// 2、解析token, 校验用户是否存在
			String uniqueName = null;
			try {
				JwtUserInfo userInfo = (JwtUserInfo) jwtTokenService.getInfoFromToken(token);
				if (userInfo == null || StringUtils.isEmpty(userInfo.getUniqueName())) {
					return ApiResponseData.unauthorizedError("用户不存在");
				}
				uniqueName = userInfo.getUniqueName();
			} catch (ExpiredJwtException e) {
				log.error("token已过期", e);
				return ApiResponseData.unauthorizedError("token已过期");
			} catch (Exception e) {
				log.error("token解析错误", e);
				return ApiResponseData.unauthorizedError("无效的accessToken，请重新登录");
			}

			// 3、校验token是否有效
			String redisToken = cacheService.getJwtToken(uniqueName);
			if (StringUtils.isBlank(redisToken)) {
				return ApiResponseData.unauthorizedError("无效的accessToken，请重新登录");
			}

			// 4、校验用户是否在其它地方登陆
			if (!StringUtils.equals(token, redisToken)) {
				return ApiResponseData.unauthorizedError("您的账号已在其他设备登录");
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ApiResponseData.innerError("系统错误");
		}

		return chain.handleAuth(chain, handlerMethod, request, response);
	}

}
