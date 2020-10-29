package com.xwj.lock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * ZooKeeper分布式锁-简单实现
 */
public class ZooKeeperSimpleLock {

	private ZkClient zkClient = new ZkClient("localhost:2181", 1000, 1000, new SerializableSerializer());

	/** 计数器 */
	private CountDownLatch cdl;

	private static final String LOCK_PATH = "/simple_lock";

	public ZooKeeperSimpleLock() {
	}

	/**
	 * 阻塞式加锁
	 */
	public void lock() {
		if (tryLock()) {
			// 尝试加锁
			return;
		}

		// 如果没加上，阻塞当前线程
		waitForLock();

		// 递归调用加锁
		lock();
	}

	private void waitForLock() {
		// 创建一个监听器
		IZkDataListener listener = new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("收到节点删除事件，被删除的节点：" + dataPath);
				if (cdl != null) {
					cdl.countDown();
				}
			}

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println("收到节点数据修改事件，修改数据的节点：" + dataPath + "，数据：" + data);
			}
		};

		// 1、给节点增加监听器
		zkClient.subscribeDataChanges(LOCK_PATH, listener);

		// 2、则阻塞住，直到前面节点被删除
		cdl = new CountDownLatch(1);
		try {
			cdl.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 3、解除节点的监听器
		zkClient.unsubscribeDataChanges(LOCK_PATH, listener);
	}

	/**
	 * 非阻塞式加锁
	 */
	public boolean tryLock() {
		if (zkClient == null) {
			return false;
		}
		try {
			zkClient.createEphemeral(LOCK_PATH, "1");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 解锁
	 */
	public void unlock() {
		if (zkClient != null) {
			zkClient.delete(LOCK_PATH);
			zkClient.close();
		}
	}

}
