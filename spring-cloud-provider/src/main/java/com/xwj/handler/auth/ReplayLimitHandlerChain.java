package com.xwj.handler.auth;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.xwj.annotations.ReplayLimit;
import com.xwj.auth.LoginInfoService;
import com.xwj.common.ApiResponseData;
import com.xwj.handler.AbstractHandler;
import com.xwj.handler.HandlerChain;
import com.xwj.utils.CommonUtil;
import com.xwj.utils.MD5Util;

import lombok.extern.slf4j.Slf4j;

/**
 * 防重放攻击校验
 */
@Slf4j
@Component
public class ReplayLimitHandlerChain extends AbstractHandler {

	@Autowired
	private LoginInfoService loginInfoService;

	@Override
	public boolean isSupport(HandlerMethod handlerMethod) {
		Method method = handlerMethod.getMethod();
		return method.isAnnotationPresent(ReplayLimit.class);
	}

	@Override
	public ApiResponseData handleAuth(HandlerChain chain, HandlerMethod handlerMethod, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("校验重放攻击");
		try {
			String requestPath = request.getRequestURI();
			Long timestamp = Long.valueOf(request.getHeader("Timestamp"));// 页面传的参数
			String nonce = request.getHeader("Nonce");// 页面传的参数,随机数,用来防止重放

			// 1、校验是否缺少参数
			String hs = request.getHeader("HS");// 请求头签名,用来防止重放
			log.info("HS:{}", hs);
			if (StringUtils.isEmpty(hs)) {
				return ApiResponseData.unauthorizedError("请求缺少HS");
			}

			// 2、校验请求头中的timestamp与nonce是否被篡改
			String hsKey = MD5Util.md5(timestamp + "_" + nonce);
			try {
				if (!StringUtils.equals(hsKey, hs)) {
					return ApiResponseData.unauthorizedError("请求错误");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

			// 3、校验timestamp是否过期
			ReplayLimit replayLimit = handlerMethod.getMethodAnnotation(ReplayLimit.class); // 方法注解
			if (!CommonUtil.checkTimestamp(timestamp, replayLimit.time())) {
				return ApiResponseData.unauthorizedError("请求过期");
			}

			// 4、校验nonce是否已存在，从而判断请求是否重放
			StringBuilder builder = new StringBuilder();
			builder.append(requestPath).append(timestamp).append(nonce);
			String md5Key = MD5Util.md5(builder.toString());
			String result = loginInfoService.getRequestInfo(md5Key);
			loginInfoService.setRequestInfo(md5Key);
			if (StringUtils.isNotEmpty(result)) {
				return ApiResponseData.unauthorizedError("请求重复");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ApiResponseData.innerError("系统错误");
		}

		return chain.handleAuth(chain, handlerMethod, request, response);
	}

}
