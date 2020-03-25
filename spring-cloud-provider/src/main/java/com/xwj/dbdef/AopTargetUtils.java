package com.xwj.dbdef;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import lombok.SneakyThrows;

public class AopTargetUtils {

	/**
	 * 获取 目标对象
	 * 
	 * @param proxy
	 *            代理对象
	 */
	@SneakyThrows
	public static Object getTarget(Object proxy) {
		if (!AopUtils.isAopProxy(proxy)) {
			return proxy;// 不是代理对象
		}
		Advised advised = (Advised) proxy;
		return advised.getTargetSource().getTarget();
	}

}