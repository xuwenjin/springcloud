package com.xwj.init;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * 所有spring bean初始化后执行
 * @author xuwenjin 2020年12月24日
 */
@Component
public class MySmartLifecycle implements SmartLifecycle {

	private AtomicBoolean running = new AtomicBoolean(false);

	/**
	 * 根据该方法的返回值决定是否执行start方法。
	 * 返回true时，start方法会被自动执行，返回false则不会。
	 */
	@Override
	public boolean isAutoStartup() {
		// 默认为true
		return false;
	}

	/**
	 * 1. 我们主要在该方法中启动任务或者其他异步服务，比如开启MQ接收消息，向服务注册中心注册服务等等
	 * 2. 当上下文被刷新（所有对象已被实例化和初始化之后）时，将调用该方法，默认生命周期处理器将检查每个SmartLifecycle对象的isAutoStartup()方法返回的布尔值。
	 * 如果为true，则该方法会被调用，而不是等待显式调用自己的start()方法。
	 */
	@Override
	public void start() {
		if (!this.running.get()) {
			// to do string
			System.out.println("MySmartLifecycle.start...");
			this.running.set(true);
		}
	}

	/**
	 * 如果工程中有多个实现接口SmartLifecycle的类，则这些类的start的执行顺序按getPhase方法返回值从小到大执行。
	 * 例如：1比2先执行，-1比0先执行。 stop方法的执行顺序则相反，getPhase返回值较大类的stop方法先被调用，小的后被调用。
	 */
	@Override
	public int getPhase() {
		// 默认为0
		return 0;
	}

	/**
	* 1. 只有该方法返回false时，start方法才会被执行。<br/>
	* 2. 只有该方法返回true时，stop(Runnable callback)或stop()方法才会被执行。
	*/
	@Override
	public boolean isRunning() {
		// 默认返回false
		return this.running.get();
	}

	/**
	 * 生命周期结束时调用的方法。当isRunning方法返回true时，该方法才会被调用。
	 */
	@Override
	public void stop() {
		System.out.println("MySmartLifecycle.stop...");
		this.running.set(false);
	}

}
