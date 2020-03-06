package com.xwj.handler.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import com.xwj.auth.AuthUtil;
import com.xwj.common.ApiResponseData;
import com.xwj.handler.AbstractHandler;
import com.xwj.handler.HandlerChain;
import com.xwj.utils.RequestUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BlackHandlerChain extends AbstractHandler {

	@Override
	public ApiResponseData handleAuth(HandlerChain chain, HandlerMethod handlerMethod, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("校验黑名单");
		try {// 判断黑名单
			String userIp = RequestUtil.getRealIpAddr(request);
			if (AuthUtil.blackLimit.contains(userIp)) {// 检查用户IP是否在黑名单中
				log.warn("该用户ip在黑名单中");
				return ApiResponseData.unauthorizedError("用户被下线,访问受限!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ApiResponseData.innerError("系统错误");
		}
		return chain.handleAuth(chain, handlerMethod, request, response);
	}

}
