package com.xwj.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.collection.IList;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.map.IMap;
import com.hazelcast.topic.ITopic;
import com.xwj.common.DistributedTopic;

/**
 * HazelCast使用
 * 
 * 
 * 1、使用管理端创建的，name=default
 * 
 * @author xuwenjin 2021年4月7日
 */
@RestController
@RequestMapping("hc")
public class HazelCastController {

//	@Autowired
	private HazelcastInstance client;

	/**
	 * 1、多个hazelcast server节点间，数据会同步
	 * 2、hazelcast没有持久化数据
	 */
	public static void main(String[] args) {
		HazelcastInstance hz = HazelcastClient.newHazelcastClient();
		IMap<String, Object> iMap = hz.getMap("my-map");
		// iMap.put("name", "John");
		// iMap.put("age", 15);
		System.out.println(iMap);
		System.out.println(iMap.get("name"));
		System.out.println(iMap.get("age"));

		ISet<Object> iSet = hz.getSet("my-set");
		// iSet.add("abc");
		// iSet.add(123);
		System.out.println(iSet);
		iSet.forEach(d -> {
			System.out.println(d);
		});

		IList<Object> iList = hz.getList("my-list");
		iList.add("abc");
		iList.add(123);
		System.out.println(iList);
		iList.forEach(d -> {
			System.out.println(d);
		});

		hz.shutdown();
	}

	/**
	 * 测试分布式Map实现
	 */
	@GetMapping("/test1")
	public void test1() {
		// 获取client name
		System.out.println(client.getName()); // hz.client_1

		String name = "default";
		// String name = "map_name_1";
		// IMap<String, Object> iMap = client.getMap(name);
		// iMap.put("name", "Jim", 60, TimeUnit.SECONDS); // 设置过期时间
		// iMap.put("age", 16);
		// iMap.put("cost", 32.5);
		// iMap.put("obj", new UserInfo("张三", "123456"));
		//
		// iMap.replace("age", 20);
		// iMap.putIfAbsent("age", 21);
		//
		// System.out.println(iMap);
		// System.out.println(iMap.get("name"));
		// System.out.println(iMap.get("age"));
		// System.out.println(iMap.get("obj"));

		client.getQueue(name);
		client.getList(name);
		client.getSet(name);
		client.getMultiMap(name);

		IMap<String, Object> iMap = client.getMap(name);
		iMap.values().forEach(d -> {
			System.out.println(d);
		});
	}

	/**
	 * 测试IExecutorService
	 */
	@GetMapping("testService")
	public void testService() throws InterruptedException, ExecutionException {
		String name = "service_1";

		client.getScheduledExecutorService(name);

		IExecutorService service = client.getExecutorService(name);
		Future<String> future = service.submit(() -> {
			System.out.println("service_1");
			return "abc";
		});
		System.out.println(future.get());
		service.shutdown();
	}

	/**
	 * 测试IdGenerator-分布式id生成器
	 */
	@GetMapping("testId")
	public void testId() {
		String name = "id_1";
		FlakeIdGenerator idGenerate = client.getFlakeIdGenerator(name);
		System.out.println(idGenerate.newId());
		/**
		 *  432442980117774339
			432442980117839875
			432442980117905411
			432442980117970947
			432442980118036483
			432442980118102019
			432442980118167555
			432442980118233091
			432442980118298627
			432442980118364163
			432442980118429699
			432442980118495235
			432442980118560771
		 */
	}

	/**
	 * 测试ITopic
	 */
	@GetMapping("testTopic")
	public void testTopic() {
		String name = "topic_1";
		ITopic<String> topic = client.getTopic(name);
		topic.addMessageListener(new DistributedTopic());
		topic.publish("Hello to distributed world");
		// 如果不销毁，那消息被消费后，还会在队列中
		topic.destroy();
	}

}
