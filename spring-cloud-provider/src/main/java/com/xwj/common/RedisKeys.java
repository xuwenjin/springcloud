package com.xwj.common;

/**
 * redis键常量类
 */
public class RedisKeys {

	public static final String LOGIN_INFO = "AUTH:loginInfo"; // 登录信息

	public static final String LAST_LOGIN_INFO = "AUTH:lastLoginInfo"; // 最近一次登录

	public static final String REQUEST_LIMIT = "AUTH:requestLimit:"; // 请求限流

	public static final String BLACK_LIMIT = "AUTH:blackLimit"; // 黑名单

}
