package com.xwj.exception;

/**
 * 权限异常
 */
public class AuthException extends RuntimeException {

	private static final long serialVersionUID = 5142660349402504499L;

	public AuthException() {
		super();
	}

	public AuthException(String message) {
		super(message);
	}

}