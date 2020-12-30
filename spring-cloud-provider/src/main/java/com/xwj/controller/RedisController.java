package com.xwj.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.xwj.entity.UserInfo;
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

	private static BloomFilter<Long> bf = BloomFilter.create(Funnels.longFunnel(), 1000000);

	@Override
	public void afterPropertiesSet() throws Exception {
		List<UserInfo> userList = userService.findAll();
		for (UserInfo user : userList) {
			bf.put(user.getId());
		}
	}

	/**
	 * 缓存雪崩----在我们设置缓存时采用了相同的过期时间，导致缓存在某一时刻同时失效，请求全部转发到DB，DB瞬时压力过重雪崩。
	 */
	@GetMapping("/findAll")
	public List<UserInfo> findAll() {
		return userService.findAll();
	}

	/**
	 * 缓存击穿----某一时间点，某一个key缓存过期，大量的并发请求过来
	 * 
	 * 解决方案：在原有的失效时间基础上增加一个随机值，比如1-5分钟随机，这样每一个缓存的过期时间的重复率就会降低，就很难引发集体失效的事件
	 */
	@GetMapping("/find")
	public UserInfo findById() {
		return userService.findById(10L);
	}

	/**
	 * 缓存击穿----某一时间点，某一个key缓存过期，大量的并发请求过来
	 */
	@GetMapping("/subcribe/{key}")
	public void testSubscribe(@PathVariable String key) {
		redisTemplate.opsForValue().set(key, key, 60, TimeUnit.SECONDS);
	}

	/**
	 * 缓存穿透----查询一个一定不存在的数据，直接穿过缓存到DB
	 * 
	 * 解决方案：
	 * 
	 * 1、使用布隆过滤器，将所有可能存在的数据哈希到一个足够大的bitmap中
	 * 
	 * 2、把这个空结果进行缓存，但它的过期时间会很短，最长不超过五分钟
	 */
	@GetMapping("/find/{id}")
	public UserInfo findById(@PathVariable Long id) {
		if (!bf.mightContain(id)) {
			// 一个一定不存在的数据，会被布隆过滤器拦住
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
	 * 测试zset排序
	 */
	@GetMapping("/zset")
	public void zset() {
		String key = "test1";

		// 添加单条
		redisTemplate.opsForZSet().add(key, 7, 1);
		redisTemplate.opsForZSet().add(key, 4, 3);
		redisTemplate.opsForZSet().add(key, 1, 5);
		redisTemplate.opsForZSet().add(key, 2, 8);
		redisTemplate.opsForZSet().add(key, 5, 11);

		// 按score排名从小打大，返回排名前三个对应value(默认)
		System.out.println("按排名从小打大:" + redisTemplate.opsForZSet().range(key, 0, 2));
		// 按score排名从大到小，返回排名前三个对应value
		System.out.println("按排名从大到小:" + redisTemplate.opsForZSet().reverseRange(key, 0, 2));

		// 在score范围内，按从小打大返回value(默认)
		System.out.println("按score范围从小打大：" + redisTemplate.opsForZSet().rangeByScore(key, 5, 9));
		// 在score范围内，按从大到小返回value
		System.out.println("按score范围从大到小：" + redisTemplate.opsForZSet().reverseRangeByScore(key, 5, 9));

		// 按时间排序
		key = "test2";
		redisTemplate.opsForZSet().add(key, 1, System.currentTimeMillis());
		Long min = System.currentTimeMillis();
		redisTemplate.opsForZSet().add(key, 2, System.currentTimeMillis());
		redisTemplate.opsForZSet().add(key, 3, System.currentTimeMillis());
		redisTemplate.opsForZSet().add(key, 4, System.currentTimeMillis());
		redisTemplate.opsForZSet().add(key, 5, System.currentTimeMillis());
		redisTemplate.opsForZSet().add(key, 6, System.currentTimeMillis());
		Long max = System.currentTimeMillis();
		redisTemplate.opsForZSet().add(key, 7, System.currentTimeMillis());
		System.out.println("指定时间范围内数据：" + redisTemplate.opsForZSet().reverseRangeByScore(key, min, max));
	}

	/**
	 * 测试list类型-left
	 */
	@GetMapping("/listLeft")
	public void listLeft() {
		String key = "listLeft";
		ListOperations<String, Object> operations = redisTemplate.opsForList();

		// 变量左边添加元素值
		// operations.leftPush(key, "a");
		// operations.leftPush(key, "b");
		// operations.leftPush(key, "c");
		// operations.leftPush(key, "d");
		// 数据顺序： d c b a
		// 数据下标： 0 1 2 3
		// 最后一位是-1

		// 获取集合指定位置的值()
		System.out.println(operations.index(key, 1));
		System.out.println(operations.index(key, -1));

		// 获取指定区间的值
		System.out.println(operations.range(key, 1, 3));
		System.out.println(operations.range(key, 1, -2));

		// 变量左边批量添加元素值
		// operations.leftPushAll(key, "d", "e");
		// System.out.println(operations.range(key, 0, -1));
		// operations.leftPushAll(key, Arrays.asList("f", "g"));
		// System.out.println(operations.range(key, 0, -1));

		// 如果key存在，则添加元素，否则不添加
		// operations.leftPushIfPresent(key, "e");
		// System.out.println(operations.range(key, 0, -1));
		// operations.leftPushIfPresent(key + "1", "haha");
		// System.out.println(operations.range(key, 0, -1));
	}

	/**
	 * 测试list类型-right
	 */
	@GetMapping("/listRight")
	public void listRight() {
		String key = "listRight";
		ListOperations<String, Object> operations = redisTemplate.opsForList();

		// 变量左边添加元素值
		// operations.rightPush(key, "a");
		// operations.rightPush(key, "b");
		// operations.rightPush(key, "c");
		// operations.rightPush(key, "d");
		// 数据顺序： a b c d
		// 数据下标： 0 1 2 3
		// 最后一位是-1

		// 获取集合指定位置的值()
		System.out.println(operations.index(key, 1));
		System.out.println(operations.index(key, -1));

		// 获取指定区间的值
		System.out.println(operations.range(key, 1, 3));
		System.out.println(operations.range(key, 1, -2));

		// 变量右边批量添加元素值
		operations.rightPushAll(key, "d", "e");
		System.out.println(operations.range(key, 0, -1));
		operations.rightPushAll(key, Arrays.asList("f", "g"));
		System.out.println(operations.range(key, 0, -1));

		// 如果存在集合则添加元素
		operations.rightPushIfPresent(key, "e");
		System.out.println(operations.range(key, 0, -1));
		operations.rightPushIfPresent(key, "haha");
		System.out.println(operations.range(key, 0, -1));
	}

	/**
	 * 测试list类型
	 */
	@GetMapping("/list")
	public void list() {
		String key = "list";
		ListOperations<String, Object> operations = redisTemplate.opsForList();

		// 变量左边添加元素值
		operations.leftPush(key, "a");
		operations.leftPush(key, "b");
		operations.leftPush(key, "c");
		operations.rightPush(key, "d");
		operations.rightPush(key, "e");
		operations.rightPush(key, "f");
		// 数据顺序： c b a d e f
		// 数据下标： 0 1 2 3 4 5
		// 最后一位是-1

		// 获取集合长度
		System.out.println(operations.size(key));

		// 移除集合左边第一个元素
		// operations.leftPop(key);
		// System.out.println(operations.range(key, 0, -1));

		// 在等待的时间里，移除集合中左边的元素，如果超过等待的时间仍没有元素则退出
		// operations.leftPop(key, 1, TimeUnit.SECONDS);
		// System.out.println(operations.range(key, 0, -1));

		// 移除集合右边第一个元素
		// operations.rightPop(key);
		// System.out.println(operations.range(key, 0, -1));

		// 在等待的时间里，移除集合中右边的元素，如果超过等待的时间仍没有元素则退出
		// operations.rightPop(key, 1, TimeUnit.SECONDS);
		// System.out.println(operations.range(key, 0, -1));

		// 移除sourceKey中右边的元素，同时将移除的元素添加在destinationKey左边 ------先进先出(可以做消息队列)
		operations.rightPopAndLeftPush(key, key);
		System.out.println(operations.range(key, 0, -1));

		// 在等待的时间里，移除sourceKey中右边的元素，同时将移除的元素添加在destinationKey左边，如果超过等待的时间仍没有元素则退出
		// ------先进先出(可以做消息队列)
		operations.rightPopAndLeftPush(key, "newList", 1, TimeUnit.SECONDS);
		System.out.println("移除后剩余元素：" + operations.range(key, 0, -1));
		System.out.println("添加后元素：" + operations.range("newList", 0, -1));

		// 在集合的指定位置插入元素,如果指定位置已有元素，则覆盖，没有则新增，超过集合下标+n则会报错
		operations.set(key, 3, "set");
		System.out.println(operations.range(key, 0, -1));

		// 从存储在键中的列表中删除等于值的元素的第一个计数事件。count> 0：删除等于从左到右移动的值的第一个元素；count<
		// 0：删除等于从右到左移动的值的第一个元素；count = 0：删除等于value的所有元素。
		operations.remove(key, 0, "b");
		System.out.println(operations.range(key, 0, -1));

		// 截取集合元素长度，保留长度内的数据
		operations.trim(key, 0, 2);
		System.out.println("截取后剩余元素：" + operations.range(key, 0, -1));
	}

}
