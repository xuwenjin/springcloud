package com.xwj.service;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.xwj.exception.HelloRetryException;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RetryService {

	// 原子性
	private static AtomicLong helloTimes = new AtomicLong();

	/**
	 * @Retryable: 默认情况下，会重试三次，重试间隔为1秒
	 * 
	 *             只有当抛出异常才会重试
	 */
	@Retryable
	public String hello() {
		long times = helloTimes.incrementAndGet();
		// 此接口，每调3次才会成功
		if (times % 3 != 0) {
			log.warn("发生异常，time={}", LocalTime.now());
			throw new HelloRetryException("发生hello异常");
		}
		return "hello";
	}

	/**
	 * 自定义重试机制
	 * 
	 * value：指定处理的异常类
	 * 
	 * maxAttempts：最大重试次数。默认3次
	 * 
	 * backoff： 重试补偿策略。默认使用@Backoff注解
	 * 
	 * 
	 * 该方法调用将会在抛出HelloRetryException异常后进行重试，最大重试次数为5，第一次重试间隔为1s，之后以2倍大小进行递增，
	 * 第二次重试间隔为2s，第三次为4s，第四次为8s。
	 */
	@Retryable(value = HelloRetryException.class, maxAttempts = 5, backoff = @Backoff(delay = 1000, multiplier = 2))
	public String hello2() {
		long times = helloTimes.incrementAndGet();
		// 此接口，每调6次才会成功
		if (times % 6 != 0) {
			log.warn("发生异常，time={}", LocalTime.now());
			throw new HelloRetryException("发生hello异常");
		}
		return "hello";
	}

	/**
	 * @Recover 用于@Retryable多次重试失败后处理方法，此注解注释的方法参数一定要是@Retryable抛出的异常，
	 *          并且返回类型也要与@Retryable的一致，否则无法识别，可以在该方法中进行日志处理
	 * 
	 */
	@Recover
	public String recoverForRetry(HelloRetryException ex) {
		log.error("重试失败");
		return null;
	}

	/**
	 * 使用guava进行重试
	 * 
	 * @return
	 */
	@SneakyThrows
	public String guavaRetry() {
		Retryer<String> retryer = RetryerBuilder.<String>newBuilder()

				// retryIf 重试条件
				.retryIfException().retryIfRuntimeException().retryIfExceptionOfType(HelloRetryException.class)
				.retryIfResult(StringUtils::isEmpty)

				// 固定等待：每次请求间隔1s
//				.withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
				// 每次等待时间递增(第一次执行等待2秒，后面每次递增1秒)
				.withWaitStrategy(WaitStrategies.incrementingWait(2, TimeUnit.SECONDS, 1, TimeUnit.SECONDS))
				// 随机等待(最大为5秒)
//				.withWaitStrategy(WaitStrategies.randomWait(5, TimeUnit.SECONDS))

				// 停止策略 : 尝试请求5次
				.withStopStrategy(StopStrategies.stopAfterAttempt(5))
				// 10s后终止
//				.withStopStrategy(StopStrategies.stopAfterDelay(10, TimeUnit.SECONDS))
				// 不终止
//				.withStopStrategy(StopStrategies.neverStop())

				// 重试监听(可配置多个，顺序执行)
				.withRetryListener(new MyRetryListener<>()).withRetryListener(new RetryListener() {
					@Override
					public <V> void onRetry(Attempt<V> attempt) {
						if (attempt.hasException()) {
							// 如果有异常，则打印出异常
							attempt.getExceptionCause().printStackTrace();
						}
					}
				})

				.build();

		return retryer.call(() -> {
			long times = helloTimes.incrementAndGet();
			log.warn("执行时间点={}", LocalTime.now());
			// 此接口，每调5次才会成功
			if (times % 5 == 1) {
				throw new NullPointerException();
			} else if (times % 5 == 2) {
				throw new Exception();
			} else if (times % 5 == 3) {
				throw new HelloRetryException("重试异常");
			} else if (times % 5 == 4) {
				return null;
			}
			return "hello";
		});

	}

}
