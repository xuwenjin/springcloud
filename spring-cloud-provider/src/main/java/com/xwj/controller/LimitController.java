package com.xwj.controller;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.redis.JsonRedisTemplate;

/**
 * 测试使用redis限流
 */
@RestController
@RequestMapping("limit")
public class LimitController {

	@Autowired
	private JsonRedisTemplate redisTemplate;

	ThreadPoolExecutor pool = null;

	/**
	 * bean实例化的时候执行
	 */
	@PostConstruct
	public void init() {
		tokenInit();
		loutongInit();
	}

	/**
	 * 令牌桶初始化
	 */
	private void tokenInit() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		// 建立一个定时任务，每隔1秒执行一次
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				String redisKey = "tokenLimitKey";
				Long count = redisTemplate.opsForList().size(redisKey);
				// 桶里只能放10个令牌
				if (count != null && count < 10) {
					System.out.println(new Date() + "----->生成一个令牌");
					// 定时生成令牌，并放入redis中
					redisTemplate.opsForList().leftPush(redisKey, UUID.randomUUID().toString());
				}
			}
		}, 0, 1L, TimeUnit.SECONDS);
	}

	/**
	 * 漏桶初始化
	 */
	private void loutongInit() {
		pool = new ThreadPoolExecutor(0, 10, 5, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	}

	/**
	 * 计数器(过期机制)
	 * 
	 * 5秒内，只允许通过10个请求
	 */
	@GetMapping("/count")
	public void count() {
		String redisKey = "countLimitKey";
		try {
			// increment(): 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作，否则直接+1
			// 返回执行+1之后的结果
			Long count = redisTemplate.boundValueOps(redisKey).increment();
			if (null != count && count == 1) {
				redisTemplate.expire(redisKey, 5L, TimeUnit.SECONDS);
			}

			// 防止出现并发操作未设置超时时间的场景，这样key就是永不过期，存在风险
			if (redisTemplate.getExpire(redisKey, TimeUnit.SECONDS) == -1) {
				// 设置永不过期的时间
				redisTemplate.expire(redisKey, 5L, TimeUnit.SECONDS);
			}

			if (count != null && count > 10) {
				System.out.println("操作太频繁");
				return;
			}
			System.out.println(count);
		} catch (Exception e) {
			// 出现故障时，删除key
			redisTemplate.delete(redisKey);
		}
	}

	/**
	 * 滑动窗口(使用zset，可以通过时间排序)
	 * 
	 * 缺点：zset结构，没有超时时间，redis中的数据会越来越多
	 * 
	 * 5秒内，只允许通过10个请求
	 */
	@GetMapping("/huadong")
	public void huadong() {
		String redisKey = "huadongLimitKey";
		Long nowMills = System.currentTimeMillis();

		Integer count = redisTemplate.opsForZSet().rangeByScore(redisKey, nowMills - 5000, nowMills).size();
		if (count != null && count >= 10) {
			System.out.println("操作太频繁");
			return;
		}
		System.out.println(count);

		redisTemplate.opsForZSet().add(redisKey, UUID.randomUUID(), System.currentTimeMillis());
	}

	/**
	 * 令牌桶：以某种恒定的速度生成令牌，并存入令牌桶中，而每个请求需要先获取令牌才能执行，如果没有获取到令牌的请求可以选择等待或者放弃执行
	 * 
	 * list结构，从左边push令牌，右边pop令牌
	 * 
	 * 5秒内，只允许通过10个请求
	 */
	@GetMapping("/token")
	public void token() {
		String redisKey = "tokenLimitKey";
		Object result = redisTemplate.opsForList().rightPop(redisKey);
		if (result == null) {
			System.out.println("操作太频繁");
			return;
		}
		System.out.println(result);
	}

	/**
	 * 漏桶算法：无论上面的水流倒入漏斗有多大，也就是无论请求有多少，它都是以均匀的速度慢慢流出的。当上面的水流速度大于下面的流出速度时，漏斗
	 * 会慢慢变满，当漏斗满了之后就会丢弃新来的请求;当上面的水流速度小于下面流出的速度的话，漏斗永远不会被装满，并且可以一直流出
	 * 
	 * 当前时间点，最多只能处理10个请求
	 */
	@GetMapping("/loutong")
	public void loutong() {
		try {
			pool.execute(() -> {
				System.out.println(new Date() + "------>执行任务");
				try {
					TimeUnit.MILLISECONDS.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			System.out.println("操作太频繁");
		}
	}

}
