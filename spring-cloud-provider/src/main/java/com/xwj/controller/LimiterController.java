package com.xwj.controller;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.annotations.RequestLimit;
import com.xwj.enums.LimitType;

/**
 * 测试限流
 */
@RestController
@RequestMapping("limit")
public class LimiterController {

	private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

	@RequestLimit(key = "test", period = 10, count = 3, prefix = "limit", limitType = LimitType.IP)
	@GetMapping("/test")
	public int testLimiter() {
		// 意味着10秒内最多可以访问3次
		return ATOMIC_INTEGER.incrementAndGet();
	}

}