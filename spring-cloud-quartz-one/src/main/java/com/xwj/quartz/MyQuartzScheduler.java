package com.xwj.quartz;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.TriggerListener;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xwj.util.TimeUtil;

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

	private final String JOB_NAME_PREFIX = "JOB_"; // 任务名称前缀

	/**
	 * 指定时间后执行任务(只会执行一次)
	 * 
	 * @param triggerStartTime 指定时间
	 */
	@SneakyThrows
	public void addJob(Class<? extends Job> jobClass, String jobName, Date triggerStartTime, Map<String, Object> params) {
		// 使用job类名作为组名
		String groupName = jobClass.getSimpleName();

		// 创建任务触发器
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).startAt(triggerStartTime).build();

		// 将触发器与任务绑定到调度器内
		this.scheduleJob(jobClass, groupName, jobName, params, trigger);
	}

	/**
	 * 带触发器的任务(执行多次)
	 * 
	 * @param cronExpression 定时任务表达式
	 */
	@SneakyThrows
	public void addJobWithCron(Class<? extends Job> jobClass, String jobName, String cronExpression, Map<String, Object> params) {
		// 使用job类名作为组名
		String groupName = jobClass.getSimpleName();

		// 基于表达式构建触发器
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).withSchedule(cronScheduleBuilder).build();

		// 将触发器与任务绑定到调度器内
		this.scheduleJob(jobClass, groupName, jobName, params, cronTrigger);
	}

	/**
	 * 带触发器的任务，同时指定时间段(立马执行)
	 * 
	 * @param timeoutSeconds 超时时间(秒)
	 * @param cronExpression 定时任务表达式
	 */
	@SneakyThrows
	public void addJobWithCron(Class<? extends Job> jobClass, String jobName, String cronExpression, long timeoutSeconds, Map<String, Object> params) {
		// 使用job类名作为组名
		String groupName = jobClass.getSimpleName();

		// 计算结束时间
		Date endDate = TimeUtil.localDateTime2Date(LocalDateTime.now().plusSeconds(timeoutSeconds));

		// 基于表达式构建触发器，同时指定时间段
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).startNow().endAt(endDate).withSchedule(cronScheduleBuilder).build();

		// 将触发器与任务绑定到调度器内
		this.scheduleJob(jobClass, groupName, jobName, params, cronTrigger);
	}

	@SneakyThrows
	private void scheduleJob(Class<? extends Job> jobClass, String groupName, String jobName, Map<String, Object> params, Trigger trigger) {
		jobName = StringUtils.join(JOB_NAME_PREFIX, jobName);
		log.info("创建任务，任务名称：{}", jobName);

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
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(cronScheduleBuilder).build();
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
	 * 获取任务状态
	 */
	@SneakyThrows
	public TriggerState getJobState(TriggerKey triggerKey) {
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

	/**
	 * 注册任务监听器(全局监听器，监听所有任务)
	 */
	@SneakyThrows
	public void addJobListener(JobListener listener) {
		scheduler.getListenerManager().addJobListener(listener);
	}

	/**
	 * 为任务增加任务监听器
	 */
	@SneakyThrows
	public void addJobListener(String name, Class<? extends Job> jobClass, JobListener listener) {
		name = StringUtils.join(JOB_NAME_PREFIX, name);
		String group = jobClass.getSimpleName();
		JobKey jobKey = new JobKey(name, group);
		Matcher<JobKey> matcher = KeyMatcher.keyEquals(jobKey);
		scheduler.getListenerManager().addJobListener(listener, matcher);
	}

	/**
	 * 注册触发器监听器(全局监听器，监听所有任务)
	 */
	@SneakyThrows
	public void addTriggerListener(TriggerListener listener) {
		scheduler.getListenerManager().addTriggerListener(listener);
	}

	/**
	 * 通过group查询有多少个运行的任务
	 */
	@SneakyThrows
	public long getRunningJobCountByGroup(Class<? extends Job> jobClass) {
		String groupName = jobClass.getSimpleName();
		GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals(groupName);
		Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
		if (CollectionUtils.isNotEmpty(jobKeySet)) {
			return jobKeySet.stream().filter(d -> StringUtils.equals(d.getGroup(), groupName)).count();
		}
		return 0;
	}

}
