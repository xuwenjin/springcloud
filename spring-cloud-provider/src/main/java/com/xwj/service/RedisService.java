package com.xwj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.lock.RedisLock;

/**
 * volatile能够实现变量的可见性，不具有原子性
 * 
 * @author xuwenjin
 */
@Service
public class RedisService {

	private int number = 500;

//	private static JedisPool pool = null;

//	private RedisLock lock = new RedisLock(pool);
	
	@Autowired
	private RedisLock lock;

//	static {
//        JedisPoolConfig config = new JedisPoolConfig();
//        // 设置最大连接数
//        config.setMaxTotal(200);
//        // 设置最大空闲数
//        config.setMaxIdle(8);
//        // 设置最大等待时间
//        config.setMaxWaitMillis(1000 * 100);
//        // 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
//        config.setTestOnBorrow(true);
//        pool = new JedisPool(config, "127.0.0.1", 6379, 3000);
//    }

//	@Autowired
//	private RedisLock redisLock;

	public void setNumber(int number) {
		this.number = number;
	}

	public void seckill() {
		if (lock.lock("xwj")) {
			System.out.println(--number);
		}
		lock.unlock("xwj");
	}

}
