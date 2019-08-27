package com.xwj.exception;

/**
 * 重试异常
 * 
 * @author xwj
 */
public class HelloRetryException extends RuntimeException {

	private static final long serialVersionUID = -2056545174171212566L;

	public HelloRetryException(String message, Throwable cause) {
		super(message, cause);
	}

	public HelloRetryException(String message) {
		super(message);
	}

}
