package com.xwj.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.RedisService;

@RestController
public class RedisController {

	@Autowired
	private RedisService service;

	@GetMapping("test")
	public void testExecute() {
		service.setNumber(500);
		ExecutorService executor = Executors.newFixedThreadPool(10);
		IntStream.range(0, 500).forEach(index -> {
			Future<?> future = executor.submit(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					service.seckill();
					return null;
				}
			});
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		});
	}

	@GetMapping("test2")
	public void testExecute2() {
		service.setNumber(500);
		for (int i = 0; i < 500; i++) {
			ThreadA threadA = new ThreadA(service);
			threadA.start();
		}
	}

	@GetMapping("test1")
	public void testExecute1() {
		service.seckill();
	}

}
