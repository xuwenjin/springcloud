package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.collection.IList;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

/**
 * HazelCast使用
 * 
 * @author xuwenjin 2021年4月7日
 */
@RestController
@RequestMapping("hc")
public class HazelCastController {

	@Autowired
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
	 * 测试1
	 */
	@GetMapping("/test1")
	public void test1() {
		IMap<String, Object> iMap = client.getMap("test-map");
		iMap.put("name", "Jim");
		iMap.put("age", 16);
		System.out.println(iMap);
		System.out.println(iMap.get("name"));
		System.out.println(iMap.get("age"));
	}

}
