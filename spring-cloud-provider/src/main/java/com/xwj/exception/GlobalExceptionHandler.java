package com.xwj.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.xwj.common.ResponseData;

/**
 * 全局异常捕获处理
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

	/**
	 * 限流异常
	 */
	@ExceptionHandler(LimitException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseData limitException(LimitException ex) {
		return ResponseData.innerError(ex.getMessage());
	}

	/**
	 * 权限异常
	 */
	@ExceptionHandler(AuthException.class)
	public ResponseData authException(AuthException ex) {
		return ResponseData.innerError(ex.getMessage());
	}

}
