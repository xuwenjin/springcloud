package com.xwj.controller;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.lock.ZooKeeperLock;
import com.xwj.lock.ZooKeeperSimpleLock;

/**
 * 测试ZooKeeper
 */
@RestController
@RequestMapping("zookeeper")
public class ZooKeeperController {

	@Autowired
	private ZkClient zkClient;

	private Integer num = 1000;

	@GetMapping("/watch")
	public void watchTest() throws InterruptedException {
		// 1、创建一个持久化znode
		String path = "/hello";
		zkClient.createPersistent(path);

		// 2、创建一个监听器
		IZkDataListener listener = new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("收到节点删除事件，被删除的节点：" + dataPath);
			}

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println("收到节点数据修改事件，修改数据的节点：" + dataPath + "，数据：" + data);
			}
		};

		// 3、给该节点增加监听器
		zkClient.subscribeDataChanges(path, listener);
	}

	/**
	 * 测试zk分布式锁
	 */
	@GetMapping("testLock")
	public void testLock() throws InterruptedException {
		// ZooKeeperLock lock = new ZooKeeperLock();
		ZooKeeperSimpleLock lock = new ZooKeeperSimpleLock();

		lock.lock();
		String s = Thread.currentThread().getName();
		if (num > 0) {
			System.out.println(s + "排号成功，号码是：" + num);
			num = num - 1;
		} else {
			System.out.println(s + "排号失败,号码已经被抢光");
		}
		lock.unlock();
	}

	/**
	 * 测试zk分布式锁
	 */
	@GetMapping("testUnLock")
	public void testUnLock() throws InterruptedException {
		ZooKeeperLock lock = new ZooKeeperLock();
		lock.unlock();
	}

}
