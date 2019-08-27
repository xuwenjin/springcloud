package com.xwj.service;

import java.util.concurrent.ExecutionException;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;

import lombok.extern.slf4j.Slf4j;

/**
 * 重试监听(可以用于异步记录错误日志)
 * 
 * @author xwj
 */
@Slf4j
public class MyRetryListener<V> implements RetryListener {

	@SuppressWarnings("hiding")
	@Override
	public <V> void onRetry(Attempt<V> attempt) {
		// 第几次重试,(注意:第一次重试其实是第一次调用)
		log.info("[retry]time=" + attempt.getAttemptNumber());

		// 距离第一次重试的延迟
		log.info("delay=" + attempt.getDelaySinceFirstAttempt());

		// 重试结果: 是异常终止, 还是正常返回
		log.info("hasException=" + attempt.hasException());
		log.info("hasResult=" + attempt.hasResult());

		// 是什么原因导致异常
		if (attempt.hasException()) {
			log.info("causeBy=" + attempt.getExceptionCause().toString());
		} else {
			// 正常返回时的结果
			log.info("result=" + attempt.getResult());
		}

		// bad practice: 增加了额外的异常处理代码
		try {
			V result = attempt.get();
			log.info("rude get=" + result);
		} catch (ExecutionException e) {
			log.error("this attempt produce exception." + e.getCause().toString());
		}
		
		log.info("log listen over.");
	}

}
