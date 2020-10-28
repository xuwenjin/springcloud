package com.xwj.lock;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.lang3.StringUtils;

public class ZooKeeperLock {

	private ZkClient zkClient = new ZkClient("localhost:2181", 1000, 1000, new SerializableSerializer());

	/** 上一个节点 */
	private String prePath;
	/** 当前节点 */
	private String currentPath;
	/** 计数器 */
	private CountDownLatch cdl;

	private static final String PRE_LOCK_PATH = "/lock";

	public ZooKeeperLock() {
		// 判断有没有/lock节点，没有则创建
		if (!zkClient.exists(PRE_LOCK_PATH)) {
			zkClient.createPersistent(PRE_LOCK_PATH);
		}
	}

	/**
	 * 阻塞式加锁
	 * 
	 * @param lockName
	 *            锁名称
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

		if (zkClient.exists(prePath)) {
			// 如果存在前面节点
			// 1、给排在前面的节点增加监听器
			zkClient.subscribeDataChanges(prePath, listener);

			// 2、则阻塞住，直到前面节点被删除
			cdl = new CountDownLatch(1);
			try {
				cdl.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// 3、解除前面节点的监听器
			zkClient.unsubscribeDataChanges(prePath, listener);
		}
	}

	/**
	 * 非阻塞式加锁
	 * 
	 * @param lockName
	 *            锁名称
	 */
	public boolean tryLock() {
		String lockPath = PRE_LOCK_PATH + "/";

		// 如果currentPath为空，则为第一尝试加锁
		if (StringUtils.isEmpty(currentPath)) {
			// 第一次尝试加锁，创建一个临时有序的节点，并赋值currentPath
			currentPath = zkClient.createEphemeralSequential(lockPath, 1);
		}

		// 获取所有临时节点并排序。临时节点为自正常的字符串，如000000001
		List<String> children = zkClient.getChildren(PRE_LOCK_PATH);
		Collections.sort(children);

		if (currentPath.equals(lockPath + children.get(0))) {
			// 如果当前节点在所有有序节点中排名第一，则加锁成功
			return true;
		}

		// 如果当前节点在所有有序节点中不是排名第一，则获取签名的节点，并赋值prePath
		int index = Collections.binarySearch(children, currentPath.substring(lockPath.length() + 1));
		prePath = lockPath + children.get(index - 1);

		return false;
	}

	/**
	 * 解锁
	 */
	public void unlock() {
		if (zkClient != null) {
			zkClient.delete(currentPath);
			zkClient.close();
		}
	}

}
