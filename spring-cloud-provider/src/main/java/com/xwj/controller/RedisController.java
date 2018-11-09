package com.xwj.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.entity.User;
import com.xwj.redis.JsonRedisTemplate;
import com.xwj.service.IUserService;
import com.xwj.service.RedisService;

@RestController
@RequestMapping("redis")
public class RedisController {

	@Autowired
	private RedisService service;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private JsonRedisTemplate redisTemplate;

	@GetMapping("test")
	public void testExecute() {
		// service.setNumber(500);
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
		// service.setNumber(500);
		for (int i = 0; i < 500; i++) {
			ThreadA threadA = new ThreadA(service);
			threadA.start();
		}
	}

	@GetMapping("test1")
	public void testExecute1() {
		service.seckill();
	}

	/**
	 * 缓存击穿
	 */
	@GetMapping("/find")
	public User findById() {
		return userService.findById(10L);
	}
	
	/**
	 * 缓存击穿
	 */
	@GetMapping("/subcribe/{key}")
	public void testSubscribe(@PathVariable String key) {
		redisTemplate.opsForValue().set(key, key, 60, TimeUnit.SECONDS);
	}
	
}
