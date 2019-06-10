package com.xwj.controller;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.annotations.Limit;
import com.xwj.enums.LimitType;

/**
 * 测试限流
 */
@RestController
public class LimiterController {

	private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

	@Limit(key = "test", period = 10, count = 3, name = "resource", prefix = "limit", limitType = LimitType.IP)
	@GetMapping("/test")
	public int testLimiter() {
		// 意味着10秒内最多可以访问3次
		return ATOMIC_INTEGER.incrementAndGet();
	}

}