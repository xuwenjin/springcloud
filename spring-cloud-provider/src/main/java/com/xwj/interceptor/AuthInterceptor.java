package com.xwj.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.xwj.annotations.IgnoreAuth;
import com.xwj.annotations.IgnoreEncode;
import com.xwj.annotations.ReplayLimit;
import com.xwj.cache.LoginInfoService;
import com.xwj.common.AppUserLogVO;
import com.xwj.common.ResponseData;
import com.xwj.common.RsaKey;
import com.xwj.common.SecurityResponse;
import com.xwj.utils.AESUtil;
import com.xwj.utils.CommonUtil;
import com.xwj.utils.MD5Util;
import com.xwj.utils.RSAUtil;
import com.xwj.utils.RequestUtil;
import com.xwj.utils.SignUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 权限认证拦截器
 */
@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final String REGEX = "[0-9A-Za-z]+";

	private LoginInfoService loginInfoService;

	public AuthInterceptor(LoginInfoService loginInfoService) {
		this.loginInfoService = loginInfoService;
	}

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
		String userIp = RequestUtil.getRealIpAddr(request);

		if (method.isAnnotationPresent(IgnoreAuth.class)) {// 免鉴权
			// 免鉴权的接口一般都是私有接口已经登录注册忘记密码等接口
			return true;
		}

		try {
			ResponseData responseData = null;
			String appId = request.getHeader("AppId");
			if (StringUtils.isEmpty(appId)) {
				appId = AuthUtil.defaultAppId;
			}

			// 处理黑名单
			if (null == responseData) {
				responseData = handleBlack(userIp, appId);
			}

			// 处理AppId
			if (null == responseData) {
				responseData = handleAppId(appId);
			}

			// 处理基本权限校验
			if (null == responseData) {
				responseData = handleBaseAuth(request, response, method, userIp);
			}

			// 处理重放攻击
			if (null == responseData) {
				responseData = handleReplay(handlerMethod, request, response, method, appId);
			}

			// 将结果写入response
			if (null != responseData) {
				writeResponse(request, response, responseData, method, appId);
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return true;
	}

	/**
	 * 处理黑名单
	 */
	private ResponseData handleBlack(String userIp, String appId) {
		try {// 判断黑名单
			if (!"eyc_app_20190918".equals(appId)) {
				return null;// 只针对e约车
			}
			if (AuthUtil.blackLimit.contains(userIp)) {// 检查用户IP是否在黑名单中
				log.warn("该用户ip在黑名单中");
				return ResponseData.unauthorizedError("用户被下线,访问受限!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseData.innerError("后台访问出错");
		}
		return null;
	}

	/**
	 * 处理AppId
	 */
	private ResponseData handleAppId(String appId) {
		try {
			if (!(AuthUtil.rsaKeyMap.containsKey(appId))) {// 如果appId不在配置的范围内直接退出访问
				log.warn("appId错误");
				return ResponseData.unauthorizedError("用户被下线,请重新登录!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseData.innerError("后台访问出错");
		}
		return null;
	}

	/**
	 * 处理重放攻击逻辑
	 * 
	 * Timestamp + Nonce
	 */
	private ResponseData handleReplay(HandlerMethod handlerMethod, HttpServletRequest request,
			HttpServletResponse response, Method method, String appId) {
		if (!method.isAnnotationPresent(ReplayLimit.class)) {
			// 只对加了ReplayLimit注解的判断是否重放攻击
			return null;
		}
		try {
			String requestPath = request.getRequestURI();
			Long timestamp = Long.valueOf(request.getHeader("Timestamp"));// 页面传的参数
			String nonce = request.getHeader("Nonce");// 页面传的参数,随机数,用来防止重放

			// 1、校验是否缺少参数
			String hs = request.getHeader("HS");// 请求头签名,用来防止重放
			if (StringUtils.isEmpty(hs)) {
				return ResponseData.unauthorizedError("请求缺少HS");
			}

			// 2、校验请求头中的timestamp与nonce是否被篡改
			String hsKey = MD5Util.md5(timestamp + "_" + nonce);
			try {
				if (!StringUtils.equals(hsKey, hs)) {
					return ResponseData.unauthorizedError("请求错误");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

			// 3、校验timestamp是否过期
			ReplayLimit replayLimit = handlerMethod.getMethodAnnotation(ReplayLimit.class); // 方法注解
			if (!CommonUtil.checkTimestamp(timestamp, replayLimit.time())) {
				return ResponseData.unauthorizedError("请求过期");
			}

			// 4、校验nonce是否已存在，从而判断请求是否重放
			String keyStr = "RequestReplayLimit-" + requestPath + "-" + timestamp + "-" + nonce;
			String md5Key = MD5Util.md5(keyStr);
			String result = loginInfoService.getRequestInfo(md5Key);
			loginInfoService.setRequestInfo(md5Key);
			if (StringUtils.isNotEmpty(result)) {
				return ResponseData.unauthorizedError("请求重复");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseData.innerError("后台访问出错");
		}
		return null;
	}

	/**
	 * 处理基本权限校验
	 */
	private ResponseData handleBaseAuth(HttpServletRequest request, HttpServletResponse response, Method method,
			String userIp) {
		String path = request.getRequestURI();
		try {
			// 1、校验是否缺少参数
			String accessToken = request.getHeader("AccessToken");// 页面传的参数
			if (StringUtils.isEmpty(accessToken)) {
				response.addHeader("AccessToken", "");
				return ResponseData.unauthorizedError("AccessToken不能为空");
			} else {
				// 用响应头把请求头带回去
				if (Pattern.matches(REGEX, accessToken)) {
					response.addHeader("AccessToken", accessToken);
				}
			}

			String newTimestamp = request.getHeader("Timestamp");// 页面传的参数
			if (StringUtils.isEmpty(newTimestamp)) {
				response.addHeader("Timestamp", "");
				return ResponseData.unauthorizedError("Timestamp不能为空");
			} else {
				if (Pattern.matches(REGEX, newTimestamp)) {
					response.addHeader("Timestamp", newTimestamp);
				}
			}

			String newNonce = request.getHeader("Nonce");// 页面传的参数,随机数,用来防止重放
			if (StringUtils.isEmpty(newNonce)) {
				response.addHeader("Nonce", "");
				return ResponseData.unauthorizedError("Nonce不能为空");
			} else {
				if (Pattern.matches(REGEX, newNonce)) {
					response.addHeader("Nonce", newNonce);
				}
			}

			String newSignature = request.getHeader("Signature");// 页面传的参数
			if (StringUtils.isEmpty(newSignature)) {
				response.addHeader("Signature", "");
				return ResponseData.unauthorizedError("Signature不能为空");
			} else {
				if (Pattern.matches(REGEX, newSignature)) {
					response.addHeader("Signature", newSignature);
				}
			}

			// 2、判断accessToken是否有效
			String phone = null;
			String token = null;
			Map<String, String> account = loginInfoService.getLoginInfo(accessToken);
			if (MapUtils.isEmpty(account)) {
				setUserLog(phone, accessToken, userIp, path, "logout", "无效的accessToken");
				loginInfoService.removeLoginInfo(accessToken);
				return ResponseData.unauthorizedError("无效的accessToken");
			} else {
				phone = account.get("phone");
				token = account.get("token");
			}

			// 3、校验用户是否登录
			Map<String, String> lastLoginInfo = loginInfoService.getLastLoginInfo(phone);
			String lastAccessToken = lastLoginInfo.get("accessToken");
			if (lastLoginInfo == null || StringUtils.isEmpty(lastAccessToken)) {
				// 用户登录成功后，会记录登录信息。如果这里为空，表明用户未登录
				setUserLog(phone, accessToken, userIp, path, "logout", "用户未登陆");
				loginInfoService.removeLoginInfo(accessToken);
				return ResponseData.unauthorizedError("用户未登陆");
			}

			// 4、校验accessToken是否有效
			if (!lastAccessToken.equals(accessToken)) {// 判断最新的accessToken和界面传过来的是否一致
				setUserLog(phone, accessToken, userIp, path, "logout", "该用户已在其他APP登录，用户被下线!");
				loginInfoService.removeLoginInfo(accessToken);
				return ResponseData.unauthorizedError("该用户已在其他APP登录，用户被下线!");
			}

			// 5、校验参数是否被篡改
			// 原理就是看前台算的签名和后台算的签名是否是一致的，前提是页面的签名算法要进行保密，如果页面的签名算法泄露，后台签名就毫无意义
			if (!SignUtil.checkSignature(newSignature, token, newTimestamp, newNonce)) {
				setUserLog(phone, accessToken, userIp, path, "logout", "签名错误，用户被下线!");
				return ResponseData.unauthorizedError("签名错误");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseData.innerError("后台访问出错");
		}

		return null;
	}

	/**
	 * 后处理回调方法，实现处理器的后处理（但在渲染视图之前），
	 * 此时我们可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理， modelAndView也可能为null。
	 */
	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object handler, ModelAndView modelAndView) throws Exception {
	}

	/**
	 * 整个请求处理完毕回调方法，即在视图渲染完毕时回调， 如性能监控中我们可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理，
	 * 类似于try-catch-finally中的finally，但仅调用处理器执行链中
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
			throws Exception {
	}

	/**
	 * 重写response
	 */
	private void writeResponse(HttpServletRequest request, HttpServletResponse response, ResponseData responseData,
			Method method, String appId) {
		if (response == null) {
			return;
		}
		String path = request.getRequestURI();
		String userIp = RequestUtil.getRealIpAddr(request);

		String responseStr = JSON.toJSONString(responseData);
		log.info("响应:{}", responseStr);
		byte[] bytes = JSON.toJSONBytes(responseData);

		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		try {
			// 如果秘钥不包含appId就返回
			RsaKey rsaKey = AuthUtil.rsaKeyMap.get(appId);
			if (rsaKey == null) {
				responseData = ResponseData.unauthorizedError("appId不存在,访问受限");
				setUserLog("", "", userIp, path, "logout", "appId不存在,访问受限");
				log.error("appId不存在,访问受限");
			} else {
				// 不启用加密传输并且不忽略加密
				if (AuthUtil.security.get() && !method.isAnnotationPresent(IgnoreEncode.class)
						&& responseData.isSuccess()) {
					SecurityResponse securityResponse = new SecurityResponse();
					// 1、生成AES秘钥
					String key = CommonUtil.generateKey();
					// 2、AES用秘钥加密
					String data = AESUtil.encode(key, responseStr);
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

	/**
	 * 设置用户日志
	 */
	public void setUserLog(String phone, String accessToken, String userIp, String url, String type, String msg) {
		try {
			if (StringUtils.isEmpty(phone)) {
				phone = "未知";
			}
			AppUserLogVO appUserLog = new AppUserLogVO();
			appUserLog.setMobile(phone);
			appUserLog.setIp(userIp);
			appUserLog.setToken(accessToken);
			appUserLog.setUrl(url);
			appUserLog.setActionType(type);
			appUserLog.setActionTime(new Date());
			appUserLog.setActionResult(msg);
			log.info("用户日志：{}", appUserLog);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
