package com.xwj.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.ParallelService;

import lombok.extern.slf4j.Slf4j;

/**
 * api
 */
@Slf4j
@RestController
@RequestMapping("api")
public class ApiController {

	@Autowired
	private ParallelService parallelService;

	/**
	 * 同时访问三个有相同功能的api，要求将执行最快的结果返回
	 */
	@GetMapping("/getFastApi")
	public String getFastApi() {
		System.out.println("-1111-");
		ExecutorService executor = Executors.newFixedThreadPool(3); // 开启3个线程	
		List<Future<String>> futureList = new ArrayList<>();
		IntStream.range(0, 3).forEach(index -> {
			Future<String> task = executor.submit(() -> request(index));
			futureList.add(task);
		});
		System.out.println("-2222-");
		for (Future<String> future : futureList) {
			try {
				System.out.println("-3333-");
				return future.get();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		// 停止接收新任务，原来的任务继续执行
		executor.shutdown();
		System.out.println("-4444-");

		return null;
	}

	private String request(int index) {
		if (index == 0) {
			return parallelService.requestA();
		}
		if (index == 1) {
			return parallelService.requestB();
		}
		if (index == 2) {
			return parallelService.requestC();
		}
		return null;
	}

}
