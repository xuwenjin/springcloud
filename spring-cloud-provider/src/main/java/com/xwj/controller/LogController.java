package com.xwj.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试log日志
 * 
 * @author xwj
 */
@Slf4j
@RestController
@RequestMapping("log")
public class LogController {

	@RequestMapping(value = "print")
	public void login() {
		log.debug("输出DEBUG级别的日志");
		log.info("输出INFO级别的日志");
		log.error("输出ERROR级别的日志");
	}

}
