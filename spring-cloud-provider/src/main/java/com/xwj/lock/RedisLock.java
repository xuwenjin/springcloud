package com.xwj.lock;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

/**
 * redis实现分布式锁
 * 
 * @author xwj
 */
@Component
public class RedisLock {

	private static final String LOCK_SUCCESS = ObjectUtils.CONST("OK");
	private static final String SET_IF_NOT_EXIST = ObjectUtils.CONST("NX");
	private static final String SET_WITH_EXPIRE_TIME = ObjectUtils.CONST("PX");
	private static final long ACQUIRE_TIMEOUT = ObjectUtils.CONST(10000); // 等待获取锁的时间
	private static final long TIMEOUT = ObjectUtils.CONST(500); // 获取锁后过期时间

	@Autowired
	private JedisPool jedisPool;

	private final static ThreadLocal<String> ID_STORE = new ThreadLocal<String>();

	/**
	 * 加锁
	 */
	public boolean lock(String lockName) {
		String lockKey = "lock:" + lockName;
		Jedis jedis = null;
		try {
			long start1 = System.currentTimeMillis();
			jedis = jedisPool.getResource();
			System.out.println("获取连接池时间:" + (System.currentTimeMillis() - start1));
			// 随机生成一个id，用于解锁
			String identifier = UUID.randomUUID().toString();
			// 尝试获取锁的时间，超时返回false
			long end = System.currentTimeMillis() + ACQUIRE_TIMEOUT;
			while (System.currentTimeMillis() < end) {
				/*
				 * 1.当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的客户端。 
				 * 2.已有锁存在，不做任何操作，返回null
				 */
				String result = jedis.set(lockKey, identifier, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, TIMEOUT);
				if (LOCK_SUCCESS.equals(result)) {
					ID_STORE.set(identifier);
					return true;
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return false;
	}

	/**
	 * 解锁
	 */
	public boolean unlock(String lockName) {
		String lockKey = "lock:" + lockName;
		Jedis jedis = null;
		boolean retFlag = false;
		try {
			jedis = jedisPool.getResource();
			while (true) {
				// 被 WATCH的键会被监视，并会发觉这些键是否被改动过了。
				// 如果有至少一个被监视的键在 EXEC执行之前被修改了，那么整个事务都会被取消
				jedis.watch(lockKey);
				// 通过前面返回的value值判断是不是该锁，若是该锁，则删除，释放锁
				if (StringUtils.equals(ID_STORE.get(), jedis.get(lockKey))) {
					// 开启事物
					Transaction transaction = jedis.multi();
					transaction.del(lockKey);
					// EXEC命令负责触发并执行事务中的所有命令
					// 返回是一个数组， 数组中的每个元素都是执行事务中的命令所产生的返回值
					List<Object> results = transaction.exec();
					if (results == null) {
						continue;
					}
					retFlag = true;
				}
				// UNWATCH命令可以手动取消对所有键的监视
				jedis.unwatch();
				break;
			}
		} catch (JedisException e) {
			throw new JedisException(e);
		} finally {
			if (jedis != null) {
				jedis.close();
				ID_STORE.remove();
			}
		}
		return retFlag;
	}

}
