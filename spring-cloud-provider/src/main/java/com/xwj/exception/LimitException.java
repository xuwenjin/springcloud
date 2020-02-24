package com.xwj.exception;

/**
 * 限流异常
 * 
 * @author xwj
 */
public class LimitException extends RuntimeException {

	private static final long serialVersionUID = -12446152611836237L;

	public LimitException() {
		super();
	}

	public LimitException(String message) {
		super(message);
	}

}