package com.xwj.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.TriggerKey;
import org.quartz.listeners.JobListenerSupport;

import com.xwj.quartz.MyQuartzScheduler;

import lombok.extern.slf4j.Slf4j;

/**
 * MyJob3任务监听器
 * 
 * 监听每一次任务的执行，也就是每一次执行任务，该监听器都会执行。不过，只有当最后一次执行时，任务的状态才是COMPLETE，其它时候都是NORMAL
 */
@Slf4j
public class MyJob3Listener extends JobListenerSupport {

	private MyQuartzScheduler quartzScheduler;

	public MyJob3Listener() {
		super();
	}

	public MyJob3Listener(MyQuartzScheduler quartzScheduler) {
		super();
		this.quartzScheduler = quartzScheduler;
	}

	@Override
	public String getName() {
		// 一定得返回一个值
		return "job3Listener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		log.info("job3任务监听器：任务即将执行~~~");
	}

	public void jobExecutionVetoed(JobExecutionContext inContext) {
		log.info("job3任务监听器：任务执行被否决~~~");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		if (quartzScheduler != null) {
			TriggerKey triggerKey = context.getTrigger().getKey();
			log.info("任务状态：{}", quartzScheduler.getJobState(triggerKey));
		}
		log.info("job3任务监听器：任务执行已完成~~~");
	}

}