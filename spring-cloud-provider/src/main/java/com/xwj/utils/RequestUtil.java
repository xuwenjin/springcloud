package com.xwj.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestUtil {

	private final static String UNKNOWN = "unknown";

	/**
	 * 从HttpServletRequest获取真实的ip地址
	 */
	public static String getRealIpAddr(HttpServletRequest request) {
		try {
			String ip = request.getHeader("x-forwarded-for");
			if (isValidIp(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (isValidIp(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (isValidIp(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (isValidIp(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (isValidIp(ip)) {
				ip = request.getRemoteAddr();
			}
			return ip;
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return "未知IP";
	}

	/**
	 * 从HttpHeaders获取真实的ip地址
	 */
	public static String getRealIpAddr(HttpHeaders headers) {
		String ip = headers.getFirst("x-forwarded-for");
		if (isValidIp(ip)) {
			ip = headers.getFirst("Proxy-Client-IP");
		}
		if (isValidIp(ip)) {
			ip = headers.getFirst("WL-Proxy-Client-IP");
		}
		if (isValidIp(ip)) {
			ip = headers.getFirst("HTTP_CLIENT_IP");
		}
		if (isValidIp(ip)) {
			ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
		}
		return ip;
	}

	/**
	 * 是否有效ip
	 */
	private static boolean isValidIp(String ip) {
		return StringUtils.isEmpty(ip) || (UNKNOWN.equalsIgnoreCase(ip));
	}

}