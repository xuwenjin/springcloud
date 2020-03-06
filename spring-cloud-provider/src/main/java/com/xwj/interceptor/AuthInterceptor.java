package com.xwj.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.xwj.annotations.IgnoreAuth;
import com.xwj.annotations.IgnoreEncode;
import com.xwj.auth.AuthUtil;
import com.xwj.auth.LoginInfoService;
import com.xwj.common.ApiResponseData;
import com.xwj.common.RsaKey;
import com.xwj.common.SecurityResponse;
import com.xwj.handler.HandlerChain;
import com.xwj.handler.auth.AppIdHandlerChain;
import com.xwj.handler.auth.BaseAuthHandlerChain;
import com.xwj.handler.auth.BlackHandlerChain;
import com.xwj.handler.auth.ReplayLimitHandlerChain;
import com.xwj.handler.auth.RequestLimitHandlerChain;
import com.xwj.utils.AESUtil;
import com.xwj.utils.CommonUtil;
import com.xwj.utils.RSAUtil;
import com.xwj.utils.RequestUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 权限认证拦截器
 */
@Slf4j
@Service
public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private LoginInfoService loginInfoService;
	@Autowired
	private BaseAuthHandlerChain baseAuthHandlerChain;
	@Autowired
	private BlackHandlerChain blackHandlerChain;
	@Autowired
	private AppIdHandlerChain appIdHandlerChain;
	@Autowired
	private ReplayLimitHandlerChain replayLimitHandlerChain;
	@Autowired
	private RequestLimitHandlerChain requestLimitHandlerChain;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		if (!AuthUtil.auth.get()) {// 不启用鉴权
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();

		if (method.isAnnotationPresent(IgnoreAuth.class)) {// 免鉴权
			// 免鉴权的接口一般都是私有接口已经登录注册忘记密码等接口
			return true;
		}

		// 使用责任链模式鉴权
		HandlerChain chain = new HandlerChain();
		chain.addHandler(blackHandlerChain);
		chain.addHandler(appIdHandlerChain);
		chain.addHandler(baseAuthHandlerChain);
		chain.addHandler(replayLimitHandlerChain);
		chain.addHandler(requestLimitHandlerChain);
		ApiResponseData responseData = chain.handleAuth(chain, handlerMethod, request, response);
		if (!responseData.isSuccess()) {
			String appId = CommonUtil.getAppId(request.getHeader("AppId"));
			writeResponse(request, response, responseData, method, appId);
			return false;
		}

		return true;
	}

	/**
	 * 重写response
	 */
	private void writeResponse(HttpServletRequest request, HttpServletResponse response, ApiResponseData responseData,
			Method method, String appId) {
		if (response == null) {
			return;
		}
		String path = request.getRequestURI();
		String userIp = RequestUtil.getRealIpAddr(request);

		String responseStr = JSON.toJSONString(responseData);
		log.info("响应结果:{}", responseStr);
		byte[] bytes = JSON.toJSONBytes(responseData);

		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		try {
			// 如果秘钥不包含appId就返回
			RsaKey rsaKey = AuthUtil.rsaKeyMap.get(appId);
			if (rsaKey == null) {
				responseData = ApiResponseData.unauthorizedError("appId不存在,访问受限");
				loginInfoService.setUserLog("", "", userIp, path, "logout", "appId不存在,访问受限");
				log.error("appId不存在,访问受限");
			} else {
				// 不启用加密传输并且不忽略加密
				if (AuthUtil.security.get() && !method.isAnnotationPresent(IgnoreEncode.class)
						&& responseData.isSuccess()) {
					SecurityResponse securityResponse = new SecurityResponse();
					// 1、生成AES秘钥
					String key = CommonUtil.generateKey();
					// 2、AES用秘钥加密
					String data = AESUtil.encrypt(responseStr, key);
					try {
						// 3、使用服务端RSA公钥对AES秘钥加密
						key = RSAUtil.publicEncrypt(key, rsaKey.getResponsePublicKey());
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
					securityResponse.setKey(key);
					securityResponse.setData(data);
					bytes = JSON.toJSONBytes(securityResponse);
				}
			}

			response.setContentLength(bytes.length);
			ServletOutputStream stream = response.getOutputStream();
			try {
				stream.write(bytes);
				stream.flush();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				try {
					stream.close();
				} catch (Exception e2) {
					log.error(e2.getMessage(), e2);
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
