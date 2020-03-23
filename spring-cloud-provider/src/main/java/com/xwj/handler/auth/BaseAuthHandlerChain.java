package com.xwj.handler.auth;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.xwj.auth.LoginInfoService;
import com.xwj.common.ApiResponseData;
import com.xwj.handler.AbstractHandler;
import com.xwj.handler.HandlerChain;
import com.xwj.utils.CommonUtil;
import com.xwj.utils.RequestUtil;
import com.xwj.utils.SignUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BaseAuthHandlerChain extends AbstractHandler {

	private static final String REGEX = "[0-9A-Za-z]+";

	@Autowired
	private LoginInfoService loginInfoService;

	@Override
	public ApiResponseData handleAuth(HandlerChain chain, HandlerMethod handlerMethod, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("基础校验");
		String path = request.getRequestURI();
		String userIp = RequestUtil.getRealIpAddr(request);
		try {
			// 1、校验是否缺少参数
			// 页面传的参数
			String accessToken = request.getHeader("AccessToken");
			log.info("accessToken:{}", accessToken);
			if (StringUtils.isEmpty(accessToken)) {
				response.addHeader("AccessToken", "");
				return ApiResponseData.unauthorizedError("AccessToken不能为空");
			} else {
				// 用响应头把请求头带回去
				if (Pattern.matches(REGEX, accessToken)) {
					response.addHeader("AccessToken", accessToken);
				}
			}
			// 页面传的参数
			String newTimestamp = request.getHeader("Timestamp");
			log.info("Timestamp:{}", newTimestamp);
			if (StringUtils.isEmpty(newTimestamp)) {
				response.addHeader("Timestamp", "");
				return ApiResponseData.unauthorizedError("Timestamp不能为空");
			} else {
				if (Pattern.matches(REGEX, newTimestamp)) {
					response.addHeader("Timestamp", newTimestamp);
				}
			}
			// 页面传的参数,随机数,用来防止重放
			String newNonce = request.getHeader("Nonce");
			log.info("Nonce:{}", newNonce);
			if (StringUtils.isEmpty(newNonce)) {
				response.addHeader("Nonce", "");
				return ApiResponseData.unauthorizedError("Nonce不能为空");
			} else {
				if (Pattern.matches(REGEX, newNonce)) {
					response.addHeader("Nonce", newNonce);
				}
			}

			// 页面传的参数
			String newSignature = request.getHeader("Signature");
			log.info("Signature:{}", newSignature);
			if (StringUtils.isEmpty(newSignature)) {
				response.addHeader("Signature", "");
				return ApiResponseData.unauthorizedError("Signature不能为空");
			} else {
				if (Pattern.matches(REGEX, newSignature)) {
					response.addHeader("Signature", newSignature);
				}
			}

			// 2、校验timestamp是否过期
			long timestamp = Long.valueOf(newTimestamp);
			if (!CommonUtil.checkTimestamp(timestamp)) {
				// return ApiResponseData.unauthorizedError("请求过期");
			}

			// // 3、判断accessToken是否有效
			// String phone = null;
			// String token = null;
			// Map<String, String> account =
			// loginInfoService.getLoginInfo(accessToken);
			// if (MapUtils.isEmpty(account)) {
			// loginInfoService.setUserLog(phone, accessToken, userIp, path,
			// "logout", "无效的accessToken");
			// loginInfoService.removeLoginInfo(accessToken);
			// return ApiResponseData.unauthorizedError("无效的accessToken");
			// } else {
			// phone = account.get("mobile");
			// token = account.get("token");
			// }
			//
			// // 4、校验用户是否登录
			// Map<String, String> lastLoginInfo =
			// loginInfoService.getLastLoginInfo(phone);
			// log.info("lastLoginInfo:{}", lastLoginInfo);
			// String lastAccessToken = lastLoginInfo.get("accessToken");
			// if (lastLoginInfo == null ||
			// StringUtils.isEmpty(lastAccessToken)) {
			// // 用户登录成功后，会记录登录信息。如果这里为空，表明用户未登录
			// loginInfoService.setUserLog(phone, accessToken, userIp, path,
			// "logout", "用户未登陆");
			// loginInfoService.removeLoginInfo(accessToken);
			// return ApiResponseData.unauthorizedError("用户未登陆");
			// }
			//
			// // 5、校验accessToken是否有效
			// // 判断最新的accessToken和界面传过来的是否一致
			// if (!StringUtils.equals(lastAccessToken, accessToken)) {
			// loginInfoService.setUserLog(phone, accessToken, userIp, path,
			// "logout", "该用户已在其他APP登录，用户被下线!");
			// loginInfoService.removeLoginInfo(accessToken);
			// return ApiResponseData.unauthorizedError("该用户已在其他APP登录，用户被下线!");
			// }

			String token = "123321";
			String phone = "18207136675";

			// 6、校验参数是否被篡改
			// 原理就是看前台算的签名和后台算的签名是否是一致的，前提是页面的签名算法要进行保密，如果页面的签名算法泄露，后台签名就毫无意义
			if (!SignUtil.checkSignature(newSignature, token, newTimestamp, newNonce)) {
				loginInfoService.setUserLog(phone, accessToken, userIp, path, "logout", "签名错误，用户被下线!");
				return ApiResponseData.unauthorizedError("签名错误");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ApiResponseData.innerError("系统错误");
		}

		return chain.handleAuth(chain, handlerMethod, request, response);
	}

}
