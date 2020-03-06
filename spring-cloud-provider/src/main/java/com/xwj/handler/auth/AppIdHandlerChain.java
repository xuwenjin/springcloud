package com.xwj.handler.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.xwj.auth.AuthUtil;
import com.xwj.common.ApiResponseData;
import com.xwj.handler.AbstractHandler;
import com.xwj.handler.HandlerChain;
import com.xwj.utils.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppIdHandlerChain extends AbstractHandler {

	@Override
	public ApiResponseData handleAuth(HandlerChain chain, HandlerMethod handlerMethod, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			log.info("校验appId");
			String appId = CommonUtil.getAppId(request.getHeader("AppId"));
			if (!(AuthUtil.rsaKeyMap.containsKey(appId))) {// 如果appId不在配置的范围内直接退出访问
				log.warn("appId错误");
				return ApiResponseData.unauthorizedError("用户被下线,请重新登录!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ApiResponseData.innerError("系统错误");
		}

		return chain.handleAuth(chain, handlerMethod, request, response);
	}

}
