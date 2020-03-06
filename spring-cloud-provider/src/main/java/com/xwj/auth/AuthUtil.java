package com.xwj.auth;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.Sets;
import com.xwj.common.RsaKey;

/**
 * 访问控制
 */
public class AuthUtil {

	/**
	 * 开启鉴权
	 */
	public static AtomicBoolean auth = new AtomicBoolean(true);
	/**
	 * 启用加密传输
	 */
	public static AtomicBoolean security = new AtomicBoolean(true);
	/**
	 * 保存ip做为黑名单
	 */
	public static Set<String> blackLimit = Sets.newConcurrentHashSet();
	/**
	 * 保存ip做为白名单，白名单是为了设置访问ip例外的
	 */
	public static ConcurrentMap<String, String> whiteLimit = new ConcurrentHashMap<>();// 操作限制
	/**
	 * 记录appId对应的RSA秘钥
	 */
	public static ConcurrentMap<String, RsaKey> rsaKeyMap = new ConcurrentHashMap<>();// 注册操作限制

}
