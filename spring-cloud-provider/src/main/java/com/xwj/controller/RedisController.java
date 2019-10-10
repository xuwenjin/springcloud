package com.xwj.controller;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
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
public class RedisController implements InitializingBean {

	@Autowired
	private IUserService userService;

	@Autowired
	private JsonRedisTemplate redisTemplate;

	@Autowired
	private RedisLockRegistry redisLockRegistry;

	@Autowired
	private RedisLock lock; // 手写的redis锁

	@Autowired
	private RedissonClient redisson;

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
		int num = Integer.valueOf((String) redisTemplate.opsForValue().get("num"));
		if (num > 0) {
			System.out.println(s + "排号成功，号码是：" + num);
			int remind = num - 1;
			redisTemplate.opsForValue().set("num", remind + "");
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
		if (isLock) {
			int num = Integer.valueOf((String) redisTemplate.opsForValue().get("num"));
			if (num > 0) {
				System.out.println(s + "排号成功，号码是：" + num);
				int remind = num - 1;
				redisTemplate.opsForValue().set("num", remind + "");
			}
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
			int num = Integer.valueOf((String) redisTemplate.opsForValue().get("num"));
			if (num > 0) {
				System.out.println(s + "排号成功，号码是：" + num);
				int remind = num - 1;
				redisTemplate.opsForValue().set("num", remind + "");
			} else {
				System.out.println(s + "排号失败,号码已经被抢光");
			}
		}
		lock.unlock("xwj");
	}

	/**
	 * 测试redis分布式锁(有锁)-redisson
	 */
	@GetMapping("testLock3")
	public void testLock3() throws InterruptedException {
		RLock disLock = redisson.getLock("DISLOCK");
		boolean isLock;
		try {
			// 获取锁最多等待10秒，超时返回false
			// 如果获取了锁，过期时间是5秒
			isLock = disLock.tryLock(10000, 5000, TimeUnit.MILLISECONDS);
			if (isLock) {
				String s = Thread.currentThread().getName();
				int num = Integer.parseInt((String) redisTemplate.opsForValue().get("num"));
				if (num > 0) {
					System.out.println(s + "排号成功，号码是：" + num);
					int remind = num - 1;
					redisTemplate.opsForValue().set("num", remind + "");
				} else {
					System.out.println(s + "排号失败,号码已经被抢光");
				}
			}
		} finally {
			// 无论如何, 最后都要解锁
			disLock.unlock();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		redisTemplate.opsForValue().set("num", "20");
	}

}
