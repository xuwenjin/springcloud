package com.xwj.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xwj.common.ApiResponseData;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常捕获处理
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ApiResponseData exceptionHandler(HttpServletRequest request, Exception ex) {
		log.error(ex.getMessage(), ex);
		return ApiResponseData.innerError(ex.getMessage());
	}

}
