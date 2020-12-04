package com.xwj.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局任务监听器
 */
@Slf4j
public class AllJobListener extends JobListenerSupport {

	@Override
	public String getName() {
		// 一定得返回一个值
		return "allJobListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		log.info("全局任务监听器：任务即将执行~~~");
	}

	public void jobExecutionVetoed(JobExecutionContext inContext) {
		log.info("全局任务监听器：任务执行被否决~~~");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		log.info("全局任务监听器：任务执行已完成~~~");
	}

}