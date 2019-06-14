package com.xwj.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.ParallelService;

/**
 * 并行请求
 * 
 * @author xwj
 */
@RestController
@RequestMapping("parallel")
public class ParallelController {

	@Autowired
	private ParallelService parallelService;

	/**
	 * 传统请求
	 */
	@GetMapping("/test1")
	public long test1() {
		long start = System.currentTimeMillis();
		List<String> list = new ArrayList<>();
		IntStream.range(0, 3).forEach(index -> {
			list.add(request(index));
		});
		list.forEach(System.out::printf);
		System.out.println("-----\r\n");
		long end = System.currentTimeMillis();
		return end - start;
	}

	/**
	 * 并行请求
	 */
	@GetMapping("/test2")
	public long test2() {
		long start = System.currentTimeMillis();
		List<String> list = new ArrayList<>();
		IntStream.range(0, 3).parallel().forEach(index -> {
			list.add(request(index));
		});
		list.forEach(System.out::printf);
		System.out.println("-----\r\n");
		long end = System.currentTimeMillis();
		return end - start;
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
