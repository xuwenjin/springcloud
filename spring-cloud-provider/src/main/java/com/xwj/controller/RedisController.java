package com.xwj.controller;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.entity.User;
import com.xwj.lock.RedisLock;
import com.xwj.redis.JsonRedisTemplate;
import com.xwj.service.IUserService;

/**
 * 测试redis
 */
@RestController
@RequestMapping("redis")
public class RedisController {

	@Autowired
	private IUserService userService;

	@Autowired
	private JsonRedisTemplate redisTemplate;

	@Autowired
	private RedisLockRegistry redisLockRegistry;

	private int num = 20;

	@Autowired
	private RedisLock lock; // 手写的redis锁

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

	/**
	 * 测试redis分布式锁(没有锁)
	 */
	@GetMapping("testUnLock")
	public void testUnLock() throws InterruptedException {
		String s = Thread.currentThread().getName();
		if (num > 0) {
			System.out.println(s + "排号成功，号码是：" + num);
			num--;
		} else {
			System.out.println(s + "排号失败,号码已经被抢光");
		}
	}

	/**
	 * 测试redis分布式锁(有锁)
	 */
	@GetMapping("testLock")
	public void testLock() throws InterruptedException {
		Lock lock = redisLockRegistry.obtain("lock");
		boolean isLock = lock.tryLock(1, TimeUnit.SECONDS);
		String s = Thread.currentThread().getName();
		if (num > 0 && isLock) {
			System.out.println(s + "排号成功，号码是：" + num);
			num--;
		} else {
			System.out.println(s + "排号失败,号码已经被抢光");
		}
		lock.unlock();
	}

	/**
	 * 测试redis分布式锁(有锁)-手写锁
	 */
	@GetMapping("testLock2")
	public void testLock2() throws InterruptedException {
		if (lock.lock("xwj")) {
			String s = Thread.currentThread().getName();
			if (num > 0) {
				System.out.println(s + "排号成功，号码是：" + num);
				num--;
			} else {
				System.out.println(s + "排号失败,号码已经被抢光");
			}
		}
		lock.unlock("xwj");
	}

}
