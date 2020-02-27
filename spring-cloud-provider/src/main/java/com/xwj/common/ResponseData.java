package com.xwj.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResponseData implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;
	private String msg;
	private Object data;
	private static final int SUCCESS = 200;
	private static final int ERROR = 500;
	private static final int UNAUTHORIZED = 401; // 未认证

	private ResponseData(int code, Object data) {
		this.code = code;
		this.data = data;
	}

	private ResponseData(int code, String message) {
		this.code = code;
		this.msg = message;
	}

	public static ResponseData success(Object data) {
		return new ResponseData(SUCCESS, data);
	}

	public static ResponseData error(int code, String message) {
		return new ResponseData(code, message);
	}

	public static ResponseData innerError(String message) {
		return new ResponseData(ERROR, message);
	}

	public static ResponseData unauthorizedError(String message) {
		return new ResponseData(UNAUTHORIZED, message);
	}

	public boolean isSuccess() {
		return getCode() == SUCCESS;
	}

}