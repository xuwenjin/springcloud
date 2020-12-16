package com.xwj.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import com.google.common.collect.ImmutableList;
import com.xwj.redis.JsonRedisTemplate;

/**
 * redis实现分布式锁
 * 
 * 加锁和解锁都使用lua脚本，并且锁支持可重入
 */
public class MyRedisLock {

	protected long lockLeaseTime;
	private String name;
	final UUID id;

	private JsonRedisTemplate redisTemplate;

	// 解锁消息内容(redis发布订阅时会用到)
	public static final Long UNLOCK_MESSAGE = 0L;

	public MyRedisLock(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
	}

	public void setRedisTemplate(JsonRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 加锁
	 */
	public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) {
		long time = unit.toMillis(waitTime);
		long threadId = Thread.currentThread().getId();
		Long ttl = tryAcquire(leaseTime, TimeUnit.MILLISECONDS, threadId);
//		System.out.println("ttl------>" + ttl);
		if (ttl == null) {
			// 加锁成功
			return true;
		}

		long current = System.currentTimeMillis();
		time -= System.currentTimeMillis() - current;
		if (time <= 0) {
			// 如果已超过等待时间，则直接返回false，加锁失败
			return false;
		}

		while (true) {
			// 自旋加锁
			long currentTime = System.currentTimeMillis();
			ttl = tryAcquire(leaseTime, unit, threadId);
			if (ttl == null) {
				// 加锁成功
				return true;
			}

			time -= System.currentTimeMillis() - currentTime;
			if (time <= 0) {
				// 如果已超过等待时间，则直接返回false，加锁失败
				return false;
			}
		}
	}

	private Long tryAcquire(long leaseTime, TimeUnit unit, long threadId) {
		if (leaseTime != -1) {
			return tryLockInnerAsync(leaseTime, unit, threadId);
		}
		return null;
	}

	private Long tryLockInnerAsync(long leaseTime, TimeUnit unit, long threadId) {
		lockLeaseTime = unit.toMillis(leaseTime);

		String luaScript = buildLockLuaScript();
		RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);

		ImmutableList<String> keys = ImmutableList.of(name);
		String lockName = getLockName(threadId);
		
//		System.out.println("lock -------->  key:" + name +", hkey:" + lockName +", leaseTime:" + lockLeaseTime);
		
		return redisTemplate.execute(redisScript, keys, leaseTime, lockName);
	}

	/**
	 * lua加锁脚本
	 * 
	 * 存入redis是采用hash数据结构，即：K, HK, HV
	 */
	private String buildLockLuaScript() {
		/**
		 * KEYS[1] = K    --->   xwj
		 * 
		 * ARGV[1] = leaseTime    ----> 过期时长
		 * 
		 * ARGV[2] = HK   ---> uuid + ":" threadId
		 */
		return 
			// 首先用的exists 判断redis中是否存在当前key，如果不存在就等于0，然后执行hset指令，将“anyLock id:threadId 1”存储到redis中。
			// 最终redis存储的数据类似于："8743c9c0-0795-4907-87fd-6c719a6b4586:1":1
			// 最后面的一个1 是为了后面可重入做的计数统计
			"if (redis.call('exists', KEYS[1]) == 0) then " +      
	 			"redis.call('hset', KEYS[1], ARGV[2], 1); " +       
	 			"redis.call('pexpire', KEYS[1], ARGV[1]); " + 
	 			//返回空值 nil，获取锁成功
	 			"return nil; " + 
	 		"end; "
	 		// 如果同一个机器同一个线程再次来请求，这里就会执行hincrby，将value+1变成了2，然后继续设置过期时间。
			+ "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " + 
	 			"redis.call('hincrby', KEYS[1], ARGV[2], 1); " + 
	 			"redis.call('pexpire', KEYS[1], ARGV[1]); " + 
	 			//返回空值nil，重入获取锁成功
	 			"return nil; " + 
	 		"end; "
	 		// 返回锁的存活时间(表明获取锁失败)
			+ "return redis.call('pttl', KEYS[1]);";
	}

	/**
	 * 解锁
	 */
	public void unlock() {
		long threadId = Thread.currentThread().getId();
		unlockAsync(threadId);
	}
	
	public void unlockAsync(long threadId) {
		String luaScript = buildUnLockLuaScript();
		RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);

		ImmutableList<String> keys = ImmutableList.of(name, getChannelName());
		String lockName = getLockName(threadId);
		
//		System.out.println("unlock -------->  key:" + keys +", hkey:" + lockName +", leaseTime:" + lockLeaseTime);
		
		Long res = redisTemplate.execute(redisScript, keys, UNLOCK_MESSAGE, lockLeaseTime, lockName);
		
		System.out.println("unlock.res ----->" + res);
	}
	
	private String getChannelName() {
        return prefixName("redisson_lock__channel", name);
    }
    
    private static String prefixName(String prefix, String name) {
        if (name.contains("{")) {
            return prefix + ":" + name;
        }
        return prefix + ":{" + name + "}";
    }
	
	/**
	 * lua解锁脚本
	 * 
	 * 存入redis是采用hash数据结构，即：K, HK, HV
	 */
	private String buildUnLockLuaScript() {
		/**
		 * KEYS[1] = K    --->   xwj
		 * 
		 * KEYS[2] = K    --->   频道key(发布订阅)
		 * 
		 * ARGV[1] = message  --->   消息内容(发布订阅)
		 * 
		 * ARGV[2] = HK   ---> leaseTime    ----> 过期时长
		 * 
		 * ARGV[3] = HK   ---> uuid + ":" threadId
		 */
		return 
			// 判断当前key是否存在，不存在，直接返回空值nil，解锁成功
            "if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then " +
                "return nil;" +
            "end; " +
             // 计数器数量-1(可重入锁)
            "local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); " +
            // 如果计数器大于0，说明还在持有锁，重新设置过期时间
            "if (counter > 0) then " +
                "redis.call('pexpire', KEYS[1], ARGV[2]); " +
                "return 0; " +
            "else " +
            // 否则使用del指令删除key，并发布解锁成功的消息
                "redis.call('del', KEYS[1]); " +
                "redis.call('publish', KEYS[2], ARGV[1]); " +
                "return 1; "+
            "end; " +
            "return nil;";
	}
	
	protected String getLockName(long threadId) {
        return id + ":" + threadId;
    }

}
