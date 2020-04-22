package com.xwj.controller;

import java.util.List;
import java.util.Set;
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

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.xwj.entity.UserInfo;
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

	private static BloomFilter<Long> bf = BloomFilter.create(Funnels.longFunnel(), 1000000);

	@Override
	public void afterPropertiesSet() throws Exception {
		redisTemplate.opsForValue().set("num", "20");

		List<UserInfo> userList = userService.findAll();
		for (UserInfo user : userList) {
			bf.put(user.getId());
		}
	}

	/**
	 * 缓存击穿
	 */
	@GetMapping("/find")
	public UserInfo findById() {
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
	 * 缓存穿透
	 */
	@GetMapping("/find/{id}")
	public UserInfo findById(@PathVariable Long id) {
		if (!bf.mightContain(id)) {
			return null;
		}

		// 先从缓存中查询，查不到读数据库
		UserInfo user = (UserInfo) redisTemplate.opsForValue().get("" + id);
		if (user == null) {
			user = userService.findById(id);
		}
		return user;
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
	 * 测试zset排序
	 */
	@GetMapping("/zset")
	public void zset() {
		String key = "zset";

		// 添加单条
		redisTemplate.opsForZSet().add(key, 1, 5);
		redisTemplate.opsForZSet().add(key, 2, 8);
		redisTemplate.opsForZSet().add(key, 3, 11);

		Set<Object> results = redisTemplate.opsForZSet().reverseRangeByScore(key, 5, 9);
		System.out.println("results：" + results);

		// 按时间排序
		redisTemplate.opsForZSet().add(key, 1, System.currentTimeMillis());
		Long min = System.currentTimeMillis();
		redisTemplate.opsForZSet().add(key, 2, System.currentTimeMillis());
		redisTemplate.opsForZSet().add(key, 3, System.currentTimeMillis());
		redisTemplate.opsForZSet().add(key, 4, System.currentTimeMillis());
		redisTemplate.opsForZSet().add(key, 5, System.currentTimeMillis());
		redisTemplate.opsForZSet().add(key, 6, System.currentTimeMillis());
		Long max = System.currentTimeMillis();
		redisTemplate.opsForZSet().add(key, 7, System.currentTimeMillis());
		results = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
		System.out.println("指定时间范围内数据：" + results);
	}

}
