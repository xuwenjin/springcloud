package com.xwj.controller;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.lock.MyRedisLock;
import com.xwj.lock.MyRedisLock2;
import com.xwj.lock.RedisLock;
import com.xwj.redis.JsonRedisTemplate;

/**
 * 测试redis分布式锁
 */
@RestController
@RequestMapping("redislock")
public class RedisLockController implements InitializingBean {

	@Autowired
	private JsonRedisTemplate redisTemplate;
	@Autowired
	private RedisLockRegistry redisLockRegistry;
	@Autowired
	private RedisLock lock; // 手写的redis锁
	@Autowired
	private RedissonClient redisson;

	@Override
	public void afterPropertiesSet() throws Exception {
		redisTemplate.opsForValue().set("num", "20");
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
		RLock disLock = redisson.getLock("xwj");
		// 获取锁最多等待10秒，超时返回false
		// 如果获取了锁，过期时间是5秒
		boolean isLock = disLock.tryLock(10000, 5000, TimeUnit.MILLISECONDS);
		if (isLock) {
			try {
				String s = Thread.currentThread().getName();
				int num = Integer.parseInt((String) redisTemplate.opsForValue().get("num"));
				if (num > 0) {
					System.out.println(s + "排号成功，号码是：" + num);
					int remind = num - 1;
					redisTemplate.opsForValue().set("num", remind + "");
				} else {
					System.out.println(s + "排号失败,号码已经被抢光");
				}
			} finally {
				// 无论如何, 最后都要解锁
				disLock.unlock();
			}
		}
	}

	/**
	 * 测试redis分布式锁(有锁)-手写锁-支持可重入
	 */
	@GetMapping("relock")
	public void relock() throws InterruptedException {
		MyRedisLock myRedisLock = new MyRedisLock("xwj");
		myRedisLock.setRedisTemplate(redisTemplate);

		// 获取锁最多等待10秒，超时返回false
		// 如果获取了锁，过期时间是5秒
		boolean isLock = myRedisLock.tryLock(5000, 3000, TimeUnit.MILLISECONDS);
		if (isLock) {
			boolean xwj = myRedisLock.tryLock(5000, 3000, TimeUnit.MILLISECONDS);
			System.out.println("xwj------->" + xwj);
			if (xwj) {
				try {
					String s = Thread.currentThread().getName();
					int num = Integer.parseInt((String) redisTemplate.opsForValue().get("num"));
					if (num > 0) {
						System.out.println(s + "排号成功，号码是：" + num);
						int remind = num - 1;
						redisTemplate.opsForValue().set("num", remind + "");
					} else {
						System.out.println(s + "排号失败,号码已经被抢光");
					}
				} finally {
					// 无论如何, 最后都要解锁
					myRedisLock.unlock();
				}
			}
		}
		myRedisLock.unlock();
	}

	/**
	 * 测试redis分布式锁(有锁)-手写锁-支持锁续命
	 */
	@GetMapping("renewlock")
	public void renewlock() throws InterruptedException {
		MyRedisLock2 myRedisLock = new MyRedisLock2("xwj");
		myRedisLock.setRedisTemplate(redisTemplate);

		// 获取锁最多等待10秒，超时返回false
		// 如果获取了锁，过期时间是5秒
		boolean xwj = myRedisLock.tryLock(5000, 3000, TimeUnit.MILLISECONDS);
//		System.out.println("xwj------->" + xwj);
		if (xwj) {
			try {
				String s = Thread.currentThread().getName();
				int num = Integer.parseInt((String) redisTemplate.opsForValue().get("num"));
				if (num > 0) {
					System.out.println(s + "排号成功，号码是：" + num);
					int remind = num - 1;
					TimeUnit.MILLISECONDS.sleep(2000);
					redisTemplate.opsForValue().set("num", remind + "");
				} else {
					System.out.println(s + "排号失败,号码已经被抢光");
				}
			} finally {
				// 无论如何, 最后都要解锁
				myRedisLock.unlock();
			}
		}
	}

}