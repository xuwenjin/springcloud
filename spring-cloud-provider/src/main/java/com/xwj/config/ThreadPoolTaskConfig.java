package com.xwj.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置(使用@Async，需要指定TaskExecutor)
 */
@Configuration
public class ThreadPoolTaskConfig {

	/**
	 * 默认情况下，在创建了线程池后，线程池中的线程数为0
	 * 当线程数小于核心线程数时，创建线程
	 * 当线程数大于等于核心线程数，且任务队列未满时，将任务放入任务队列
	 * 当线程数大于等于核心线程数，且任务队列已满：
	 *   (1)若线程数小于等于最大线程数，创建线程。
	 *   (2)若线程数大于最大线程数，抛出异常，拒绝任务，开始使用拒绝策略拒绝
	 */

	/** 核心线程数（默认线程数） */
	private static final int CORE_POOL_SIZE = 20;
	/** 最大线程数 */
	private static final int MAX_POOL_SIZE = 100;
	/** 允许线程空闲时间（单位：默认为秒） */
	private static final int KEEP_ALIVE_TIME = 10;
	/** 缓冲队列大小 */
	private static final int QUEUE_CAPACITY = 200;
	/** 线程池名前缀 */
	private static final String THREAD_NAME_PREFIX = "Async-Thread-";

	@Bean("taskExecutor") // bean的名称，默认为首字母小写的方法名
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(CORE_POOL_SIZE);
		executor.setMaxPoolSize(MAX_POOL_SIZE);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
		executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
		executor.setAllowCoreThreadTimeOut(true); // 允许核心线程数超时(默认false)。如果为true，则也是使用keepAliveSeconds作为允许空闲时间

		// 线程池对拒绝任务的处理策略
		// CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 初始化
		executor.initialize();
		return executor;
	}
}