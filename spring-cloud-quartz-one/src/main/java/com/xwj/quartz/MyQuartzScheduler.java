package com.xwj.quartz;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Quartz任务处理
 */
@Slf4j
@Component
public class MyQuartzScheduler {

	@Autowired
	private Scheduler scheduler; // 任务

	/**
	 * 指定时间后执行任务
	 * 
	 * @param triggerStartTime
	 *            指定时间
	 */
	@SneakyThrows
	public void addJob(Class<? extends Job> jobClass, Date triggerStartTime, Map<String, Object> params) {
		// 任务名称
		String jobName = UUID.randomUUID().toString();

		// 使用job类名作为组名
		String groupName = jobClass.getSimpleName();

		// 创建任务触发器(startAt指定任务开始执行时间)
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).startAt(triggerStartTime)
				.build();

		// 将触发器与任务绑定到调度器内
		this.scheduleJob(jobClass, groupName, jobName, params, trigger);
	}

	/**
	 * 带触发器的任务
	 * 
	 * @param cronExpression
	 *            定时任务表达式
	 */
	@SneakyThrows
	public void addJobWithCron(Class<? extends Job> jobClass, String cronExpression, Map<String, Object> params) {
		// 任务名称
		String jobName = UUID.randomUUID().toString();

		// 使用job类名作为组名
		String groupName = jobClass.getSimpleName();

		// 基于表达式构建触发器
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName)
				.withSchedule(cronScheduleBuilder).build();

		// 将触发器与任务绑定到调度器内
		this.scheduleJob(jobClass, groupName, jobName, params, cronTrigger);
	}

	@SneakyThrows
	private void scheduleJob(Class<? extends Job> jobClass, String groupName, String jobName,
			Map<String, Object> params, Trigger trigger) {
		log.info("创建任务~~~");

		// 创建任务
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, groupName).build();

		// 添加参数
		jobDetail.getJobDataMap().putAll(params);

		// 将触发器与任务绑定到调度器内
		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 删除某个任务
	 */
	@SneakyThrows
	public boolean deleteJob(String name, String group) {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null) {
			throw new RuntimeException("任务不存在");
		}
		return scheduler.deleteJob(jobKey);
	}

	/**
	 * 修改某个任务的执行时间
	 */
	@SneakyThrows
	public boolean modifyJob(String name, String group, String time) {
		Date date = null;
		TriggerKey triggerKey = new TriggerKey(name, group);
		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		String oldTime = cronTrigger.getCronExpression();
		if (!oldTime.equalsIgnoreCase(time)) {
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
					.withSchedule(cronScheduleBuilder).build();
			date = scheduler.rescheduleJob(triggerKey, trigger);
		}
		return date != null;
	}

	/**
	 * 获取任务状态
	 */
	@SneakyThrows
	public TriggerState getJobState(String name, String group) {
		TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
		return scheduler.getTriggerState(triggerKey);
	}

	/**
	 * 暂停所有任务
	 */
	@SneakyThrows
	public void pauseAllJob() {
		scheduler.pauseAll();
	}

	/**
	 * 暂停某个任务
	 */
	@SneakyThrows
	public void pauseJob(String name, String group) {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null) {
			throw new RuntimeException("任务不存在");
		}
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复所有任务
	 */
	@SneakyThrows
	public void resumeAllJob() {
		scheduler.resumeAll();
	}

	/**
	 * 恢复某个任务
	 */
	@SneakyThrows
	public void resumeJob(String name, String group) {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null) {
			throw new RuntimeException("任务不存在");
		}
		scheduler.resumeJob(jobKey);
	}

}
