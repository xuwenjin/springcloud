package com.xwj.lock;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
public class MyRedisLock2 {

	/**获取锁后，过期时间*/
	protected long lockLeaseTime;

	/**传进来的锁名称*/
	private String name;

	private final String entryName;

	/**uuid*/
	private final UUID id;

	private static final ConcurrentMap<String, MyExpirationEntry> EXPIRATION_RENEWAL_MAP = new ConcurrentHashMap<>();

	private JsonRedisTemplate redisTemplate;

	// 解锁消息内容(redis发布订阅时会用到)
	public static final Long UNLOCK_MESSAGE = 0L;

	public MyRedisLock2(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.entryName = id + ":" + name;
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
		Long ttl = tryAcquire(leaseTime, unit, threadId);
		// System.out.println("ttl------>" + ttl);
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
		lockLeaseTime = unit.toMillis(leaseTime);

		String luaScript = buildLockLuaScript();
		RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);

		ImmutableList<String> keys = ImmutableList.of(name);
		String lockName = getLockName(threadId);

		// System.out.println("lock --------> key:" + name +", hkey:" + lockName
		// +", leaseTime:" + lockLeaseTime);

		Long ttl = redisTemplate.execute(redisScript, keys, lockLeaseTime, lockName);
		if (ttl == null) {
			// 加锁成功，开启续命
			scheduleExpirationRenewal(threadId);
		}
		return ttl;
	}

	/**
	 * 解锁
	 */
	public void unlock() {
		long threadId = Thread.currentThread().getId();

		String luaScript = buildUnLockLuaScript();
		RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);

		ImmutableList<String> keys = ImmutableList.of(name, getChannelName());
		String lockName = getLockName(threadId);

		// redisTemplate.execute(redisScript, keys, UNLOCK_MESSAGE,
		// lockLeaseTime, lockName);
		Long res = redisTemplate.execute(redisScript, keys, UNLOCK_MESSAGE, lockLeaseTime, lockName);
		System.out.println("unlock.res ----->" + res);
	}

	private void scheduleExpirationRenewal(long threadId) {
		MyExpirationEntry entry = new MyExpirationEntry();

		// 如果执行的key存在，则直接返回旧的value，否则替换旧的value并返回新的value
		MyExpirationEntry oldEntry = EXPIRATION_RENEWAL_MAP.putIfAbsent(entryName, entry);
		if (oldEntry != null) {
			oldEntry.incrThreadCount(threadId);
		} else {
			entry.incrThreadCount(threadId);
			renewExpiration();
		}
	}

	/**
	 * 锁-续命
	 */
	private void renewExpiration() {
		MyExpirationEntry ee = EXPIRATION_RENEWAL_MAP.get(entryName);
		if (ee == null) {
			return;
		}

		// 创建一个延时线程
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.schedule(() -> {
			MyExpirationEntry ent = EXPIRATION_RENEWAL_MAP.get(entryName);
			if (ent == null) {
				return;
			}
			Long threadId = ent.getFirstThreadId();
			if (threadId == null) {
				return;
			}

			Long res = doRenew(threadId);
			System.out.println("续命结果：" + res);
			if (res > 0) {
				// 续命成功，再开启一个续命任务
				renewExpiration();
			}
		}, lockLeaseTime / 3, TimeUnit.MILLISECONDS);
	}

	private Long doRenew(long threadId) {
		String luaScript = buildRenewLuaScript();
		RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);

		ImmutableList<String> keys = ImmutableList.of(name);
		String lockName = getLockName(threadId);

		return redisTemplate.execute(redisScript, keys, lockLeaseTime, lockName);
	}

	/**
	 * 拼锁的key。如：8743c9c0-0795-4907-87fd-6c719a6b4586:1
	 * 
	 * @param threadId 线程id
	 */
	protected String getLockName(long threadId) {
		return id + ":" + threadId;
	}

	/**
	 * 加个前缀
	 */
	private String getChannelName() {
		return "redisson_lock__channel" + ":{" + name + "}";
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
		// 首先用的exists 判断redis中是否存在当前key，如果不存在就等于0，然后执行hset指令，将“anyLock
		// id:threadId 1”存储到redis中。
		// 最终redis存储的数据类似于："8743c9c0-0795-4907-87fd-6c719a6b4586:1":1
		// 最后面的一个1 是为了后面可重入做的计数统计
		"if (redis.call('exists', KEYS[1]) == 0) then " + 
			"redis.call('hset', KEYS[1], ARGV[2], 1); " + 
			"redis.call('pexpire', KEYS[1], ARGV[1]); " +
			// 返回空值 nil，获取锁成功
			"return nil; " + 
		"end; " +
		// 如果同一个机器同一个线程再次来请求，这里就会执行hincrby，将value+1变成了2，然后继续设置过期时间。
		"if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " + 
			"redis.call('hincrby', KEYS[1], ARGV[2], 1); " + 
			"redis.call('pexpire', KEYS[1], ARGV[1]); " +
			// 返回空值nil，重入获取锁成功
			"return nil; " + 
		"end; " + 
			// 返回锁的存活时间(表明获取锁失败)
			"return redis.call('pttl', KEYS[1]);";
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
			"return 1; " + 
		"end; " + 
			"return nil;";
	}

	/**
	 * lua脚本-续命
	 * 
	 * 存入redis是采用hash数据结构，即：K, HK, HV
	 */
	private String buildRenewLuaScript() {
		/**
		 * KEYS[1] = K    --->   xwj
		 * 
		 * ARGV[1] = HK   ---> leaseTime    ----> 过期时长
		 * 
		 * ARGV[2] = HK   ---> uuid + ":" threadId
		 */
		return
		// 判断当前key是否存在，存在则重新设置过期时间
		"if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " + 
			"redis.call('pexpire', KEYS[1], ARGV[1]); " + 
			"return 1; " +
		// 当前key不存在，则返回0
		"end; " + 
			"return 0;";
	}

	public static class MyExpirationEntry {

		private final Map<Long, Integer> threadIds = new LinkedHashMap<>();

		public MyExpirationEntry() {
		}

		/**
		 * 增加当前线程对应的数量
		 */
		public void incrThreadCount(long threadId) {
			Integer counter = threadIds.get(threadId);
			if (counter == null) {
				counter = 1;
			} else {
				counter++;
			}
			threadIds.put(threadId, counter);
		}

		public boolean hasNoThreads() {
			return threadIds.isEmpty();
		}

		/**
		 * 获取下一个ThreadId
		 */
		public Long getFirstThreadId() {
			if (threadIds.isEmpty()) {
				return null;
			}
			return threadIds.keySet().iterator().next();
		}

		/**
		 * 减少当前线程对应的数量
		 */
		public void decrThreadCount(long threadId) {
			Integer counter = threadIds.get(threadId);
			if (counter == null) {
				return;
			}
			counter--;
			if (counter == 0) {
				threadIds.remove(threadId);
			} else {
				threadIds.put(threadId, counter);
			}
		}
	}

}
