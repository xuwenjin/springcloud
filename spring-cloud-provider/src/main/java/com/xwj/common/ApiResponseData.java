package com.xwj.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class ApiResponseData implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;
	private String msg;
	private Object data;
	private static final int SUCCESS = 200;
	private static final int ERROR = 500;
	private static final int UNAUTHORIZED = 401; // 未认证

	private ApiResponseData(int code, Object data) {
		this.code = code;
		this.data = data;
	}

	private ApiResponseData(int code, String message) {
		this.code = code;
		this.msg = message;
	}

	public static ApiResponseData success() {
		return new ApiResponseData(SUCCESS, null);
	}

	public static ApiResponseData success(Object data) {
		return new ApiResponseData(SUCCESS, data);
	}

	public static ApiResponseData error(int code, String message) {
		return new ApiResponseData(code, message);
	}

	/**
	 * 内部异常
	 */
	public static ApiResponseData innerError(String message) {
		return new ApiResponseData(ERROR, message);
	}

	/**
	 * 权限认证失败
	 */
	public static ApiResponseData unauthorizedError(String message) {
		return new ApiResponseData(UNAUTHORIZED, message);
	}

	/**
	 * 是否成功
	 */
	public boolean isSuccess() {
		return getCode() == SUCCESS;
	}

}