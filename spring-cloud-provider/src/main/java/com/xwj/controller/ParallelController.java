package com.xwj.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.ParallelService;

import lombok.extern.slf4j.Slf4j;

/**
 * 同时处理多个请求
 * 
 * @author xwj
 */
@Slf4j
@RestController
@RequestMapping("parallel")
public class ParallelController {

	@Autowired
	private ParallelService parallelService;

	/**
	 * 串行(传统请求)
	 */
	@GetMapping("/test1")
	public void test1() {
		long start = System.currentTimeMillis();
		List<String> list = new ArrayList<>();
		IntStream.range(0, 3).forEach(index -> {
			list.add(request(index));
		});
		log.info("串行，响应结果：{}，响应时长：{}", Arrays.toString(list.toArray()), System.currentTimeMillis() - start);
	}

	/**
	 * 并行请求
	 */
	@GetMapping("/test2")
	public void test2() {
		long start = System.currentTimeMillis();
		List<String> list = new ArrayList<>();
		IntStream.range(0, 3).parallel().forEach(index -> {
			list.add(request(index));
		});
		log.info("java8并行，响应结果：{}，响应时长：{}", Arrays.toString(list.toArray()), System.currentTimeMillis() - start);
	}

	/**
	 * 多线程请求
	 */
	@GetMapping("/test3")
	public void test3() {
		long start = System.currentTimeMillis();

		List<String> list = new ArrayList<>();
		List<Future<String>> futureList = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(3); // 开启3个线程
		IntStream.range(0, 3).forEach(index -> {
			Future<String> task = executor.submit(() -> request(index));
			futureList.add(task);
		});
		for (Future<String> future : futureList) {
			try {
				// 如果任务执行完成，future.get()方法会返回Callable任务的执行结果。
				// future.get()方法会产生阻塞，所有线程都阻塞在这里，当获取到一个结果后，才执行下一个
				list.add(future.get());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		// 停止接收新任务，原来的任务继续执行
		executor.shutdown();

		log.info("多线程，响应结果：{}，响应时长：{}", Arrays.toString(list.toArray()), System.currentTimeMillis() - start);
	}

	/**
	 * 多线程请求(带超时时间)
	 */
	@GetMapping("/test4")
	public void test4() {
		long start = System.currentTimeMillis();

		List<String> list = new ArrayList<>();
		List<Future<String>> futureList = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(3); // 开启3个线程
		IntStream.range(0, 3).forEach(index -> {
			Future<String> task = executor.submit(() -> request(index));
			futureList.add(task);
		});
		for (int i = 0; i < futureList.size(); i++) {
			Future<String> future = futureList.get(i);
			try {
				/*
				 * 如果任务执行完成，future.get()方法会返回Callable任务的执行结果。方法会产生阻塞，所有线程都阻塞在这里，当获取到一个结果后，才执行下一个
				 * future.get(timeout, unit)：在指定的时间内会等待任务执行，超时则抛异常。任务执行的时间是获取到结果的时长。
				 * 由于每个任务是同时执行的， 但是获取结果时，是阻塞的，也就是串行获取的，所以每个任务获取结果的时长 = 当前任务请求时长 - 上一个任务请求时长。
				 * 由此可以看出，当单个任务执行时，超时时间有效，当多个任务执行时，超时时间无效
				 */
				long start1 = System.currentTimeMillis();
				String result = future.get(3, TimeUnit.SECONDS);
				log.info("结果：{}，用时：{}", result, System.currentTimeMillis() - start1);
				list.add(result);
			} catch (TimeoutException e) {
				log.info("task{}，超时", i);
				// 强制取消该任务
				future.cancel(true);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		// 停止接收新任务，原来的任务继续执行
		executor.shutdown();

		log.info("多线程，响应结果：{}，响应时长：{}", Arrays.toString(list.toArray()), System.currentTimeMillis() - start);
	}

	/**
	 * 多线程请求(带超时时间)
	 */
	@GetMapping("/test5")
	public void test5() {
		long start = System.currentTimeMillis();

		List<String> list = new ArrayList<>();
		List<Future<String>> futureList = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(3); // 开启3个线程
		IntStream.range(0, 3).forEach(index -> {
			Future<String> task = executor.submit(() -> request(index));
			futureList.add(task);
		});
		for (int i = 0; i < futureList.size(); i++) {
			Future<String> future = futureList.get(i);
			ExecutorService executor2 = Executors.newSingleThreadExecutor();
			int j = i;
			executor2.execute(() -> {
				try {
					// 在指定的时间内会等待任务执行，超时则抛异常
					long start1 = System.currentTimeMillis();
					String result = future.get(2, TimeUnit.SECONDS);
					log.info("结果：{}，用时：{}", result, System.currentTimeMillis() - start1);
					list.add(result);
				} catch (TimeoutException e) {
					log.info("task{}，超时", j);
					future.cancel(true);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			});
		}

		// 停止接收新任务，原来的任务继续执行
		executor.shutdown();

		while (true) {
			// 将future.get放入子线程后，由于不会阻塞，所以就直接运行到下面。需要通过判断所有线程是否结束来获取最终结果
			if (executor.isTerminated()) {
				log.info("多线程，响应结果：{}，响应时长：{}", Arrays.toString(list.toArray()), System.currentTimeMillis() - start);
				break;
			}
		}
	}

	/**
	 * 多线程请求(带超时时间)
	 */
	@GetMapping("/test6")
	public void test6() {
		long start = System.currentTimeMillis();

		List<String> list = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(3); // 开启3个线程
		List<Callable<String>> callableList = new ArrayList<>();
		IntStream.range(0, 3).forEach(index -> {
			callableList.add(() -> request(index));
		});
		try {
			log.info("开始执行");
			long start1 = System.currentTimeMillis();
			// invokeAll会阻塞。必须等待所有的任务执行完成后统一返回，这里的超时时间是针对的所有tasks，而不是单个task的超时时间。
			// 如果超时，会取消没有执行完的所有任务，并抛出超时异常
			List<Future<String>> futureList = executor.invokeAll(callableList, 2, TimeUnit.SECONDS);
			log.info("执行完，用时：{}", System.currentTimeMillis() - start1);
			for (int i = 0; i < futureList.size(); i++) {
				Future<String> future = futureList.get(i);
				try {
					list.add(future.get());
				} catch (CancellationException e) {
					log.info("超时任务：{}", i);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (InterruptedException e1) {
			log.info("线程被中断");
		}

		// 停止接收新任务，原来的任务继续执行
		executor.shutdown();

		log.info("多线程，响应结果：{}，响应时长：{}", Arrays.toString(list.toArray()), System.currentTimeMillis() - start);
	}

	private String request(int index) {
		if (index == 0) {
			return parallelService.requestA();
		}
		if (index == 1) {
			return parallelService.requestB();
		}
		if (index == 2) {
			return parallelService.requestC();
		}
		return null;
	}

}
