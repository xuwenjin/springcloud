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

	/**初始化号码*/
	private static final String INIT_NUM = "lock_num";

	/**锁名称*/
	private static final String LOCK_NAME = "xwj";

	@Override
	public void afterPropertiesSet() throws Exception {
		redisTemplate.opsForValue().set(INIT_NUM, "10");
	}

	/**
	 * 测试redis分布式锁(没有锁)
	 */
	@GetMapping("testUnLock")
	public void testUnLock() throws InterruptedException {
		String s = Thread.currentThread().getName();
		int num = Integer.valueOf((String) redisTemplate.opsForValue().get(INIT_NUM));
		if (num > 0) {
			System.out.println(s + "----排号成功，号码是：" + num);
			int remind = num - 1;
			redisTemplate.opsForValue().set(INIT_NUM, remind + "");
		} else {
			System.out.println(s + "----排号失败，号码已经被抢光");
		}
	}

	/**
	 * 测试redis分布式锁(有锁)
	 */
	@GetMapping("testLock")
	public void testLock() throws InterruptedException {
		Lock lock = redisLockRegistry.obtain(LOCK_NAME);
		boolean isLock = lock.tryLock(1, TimeUnit.SECONDS);
		String s = Thread.currentThread().getName();
		if (isLock) {
			int num = Integer.valueOf((String) redisTemplate.opsForValue().get(INIT_NUM));
			if (num > 0) {
				System.out.println(s + "----排号成功，号码是：" + num);
				int remind = num - 1;
				redisTemplate.opsForValue().set(INIT_NUM, remind + "");
			}
		} else {
			System.out.println(s + "----排号失败，号码已经被抢光");
		}
		lock.unlock();
	}

	/**
	 * 测试redis分布式锁(有锁)-手写锁
	 */
	@GetMapping("testLock2")
	public void testLock2() throws InterruptedException {
		if (lock.lock(LOCK_NAME)) {
			String s = Thread.currentThread().getName();
			int num = Integer.valueOf((String) redisTemplate.opsForValue().get(INIT_NUM));
			if (num > 0) {
				System.out.println(s + "----排号成功，号码是：" + num);
				int remind = num - 1;
				redisTemplate.opsForValue().set(INIT_NUM, remind + "");
			} else {
				System.out.println(s + "----排号失败，号码已经被抢光");
			}
		}
		lock.unlock(LOCK_NAME);
	}

	/**
	 * 测试redis分布式锁(有锁)-redisson
	 */
	@GetMapping("testLock3")
	public void testLock3() throws InterruptedException {
		RLock disLock = redisson.getLock(LOCK_NAME);
		// 获取锁最多等待12秒，超时返回false
		// 如果获取了锁，过期时间是5秒
		String s = Thread.currentThread().getName();
		boolean isLock = disLock.tryLock(27000, 3000, TimeUnit.MILLISECONDS);
		if (isLock) {
			try {
				int num = Integer.parseInt((String) redisTemplate.opsForValue().get(INIT_NUM));
				if (num > 0) {
					// a、加锁成功，排号成功
					System.out.println(s + "--------排号成功，号码是：" + num);
					int remind = num - 1;
					TimeUnit.MILLISECONDS.sleep(4000);
					redisTemplate.opsForValue().set(INIT_NUM, remind + "");
				} else {
					// b、加锁成功，号用完了，排号失败
					System.out.println(s + "----排号失败，号码已经被抢光");
				}
			} finally {
				// 无论如何, 最后都要解锁
				disLock.unlock();
			}
		} else {
			// c、自旋超时，加锁失败(多线程时，waitTime要尽量设置大一些(默认30秒))
			System.out.println(s + "----抢票超时，请稍后重试");
		}
	}

	/**
	 * 测试redis分布式锁(有锁)-手写锁-支持可重入
	 */
	@GetMapping("relock")
	public void relock() throws InterruptedException {
		MyRedisLock myRedisLock = new MyRedisLock(LOCK_NAME);
		myRedisLock.setRedisTemplate(redisTemplate);

		// 获取锁最多等待5秒，超时返回false
		// 如果获取了锁，过期时间是3秒
		boolean isLock = myRedisLock.tryLock(5000, 3000, TimeUnit.MILLISECONDS);
		if (isLock) {
			boolean xwj = myRedisLock.tryLock(5000, 3000, TimeUnit.MILLISECONDS);
			System.out.println("xwj------->" + xwj);
			if (xwj) {
				try {
					String s = Thread.currentThread().getName();
					int num = Integer.parseInt((String) redisTemplate.opsForValue().get(INIT_NUM));
					if (num > 0) {
						System.out.println(s + "----排号成功，号码是：" + num);
						int remind = num - 1;
						redisTemplate.opsForValue().set(INIT_NUM, remind + "");
					} else {
						System.out.println(s + "----排号失败，号码已经被抢光");
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
		MyRedisLock2 myRedisLock = new MyRedisLock2(LOCK_NAME);
		myRedisLock.setRedisTemplate(redisTemplate);

		// 获取锁最多等待12秒，超时返回false
		// 如果获取了锁，过期时间是5秒
		String s = Thread.currentThread().getName();
		boolean xwj = myRedisLock.tryLock(27000, 3000, TimeUnit.MILLISECONDS);
//		System.out.println("xwj------->" + xwj);
		if (xwj) {
			try {
				int num = Integer.parseInt((String) redisTemplate.opsForValue().get(INIT_NUM));
				if (num > 0) {
					// a、加锁成功，排号成功
					System.out.println(s + "----排号成功，号码是：" + num);
					int remind = num - 1;
					TimeUnit.MILLISECONDS.sleep(4000);
					redisTemplate.opsForValue().set(INIT_NUM, remind + "");
				} else {
					// b、加锁成功，号用完了，排号失败
					System.out.println(s + "----排号失败，号码已经被抢光");
				}
			} finally {
				// 无论如何, 最后都要解锁
				myRedisLock.unlock();
			}
		} else {
			// c、自旋超时，加锁失败(多线程时，waitTime要尽量设置大一些(默认30秒))
			System.out.println(s + "----抢票超时，请稍后重试");
		}
	}

}
