package com.xwj.jwt;

public interface IJwtInfo {

	/**
	 * 获取唯一标识
	 */
	String getUniqueName();

	/**
	 * 获取用户ID
	 */
	String getId();

	/**
	 * 获取名称
	 */
	String getName();

}
