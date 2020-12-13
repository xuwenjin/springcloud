package com.xwj.quartz.listener;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.listeners.JobListenerSupport;

import com.xwj.quartz.MyQuartzScheduler;

import lombok.extern.slf4j.Slf4j;

/**
 * MyJob任务监听器
 */
@Slf4j
public class MyJobListener extends JobListenerSupport {

	private MyQuartzScheduler quartzScheduler;
	private String jobName;

	public MyJobListener() {
	}

	public MyJobListener(MyQuartzScheduler quartzScheduler) {
		this.quartzScheduler = quartzScheduler;
	}

	public MyJobListener(MyQuartzScheduler quartzScheduler, String jobName) {
		this.quartzScheduler = quartzScheduler;
		this.jobName = jobName;
	}

	@Override
	public String getName() {
		/**
		 * 一定得返回一个值(如果需要同时监听多个任务，这里的name必须与任务名称一致，否则只会监听最近一个任务)
		 */
		if (StringUtils.isNotEmpty(jobName)) {
			return jobName;
		}
		return "default";
	}

	/**
	 * 任务单次执行完一次，就会回调该方法。也就是每一次任务执行完成，该监听器都会执行。不过，只有当最后一次执行时，任务的状态才是COMPLETE，其它时候都是NORMAL
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		if (quartzScheduler != null) {
			TriggerKey triggerKey = context.getTrigger().getKey();
			TriggerState state = quartzScheduler.getJobState(triggerKey);
			log.info("name：{}，group：{}，state：{}", triggerKey.getName(), triggerKey.getGroup(), state);
			if (TriggerState.COMPLETE.equals(state)) {
				log.info("任务全部执行完成");
			}
		}
	}

}