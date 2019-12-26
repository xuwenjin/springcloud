package com.xwj.quartz.job;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

/**
 * 指定时间点触发的任务
 */
@Slf4j
public class MyJob1 extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobKey jobKey = jobDetail.getKey();
		JobDataMap dataMap = jobDetail.getJobDataMap(); // 接收参数
		log.info("执行MyJob1任务，任务名称：{}，接收参数：{}", jobKey.getName(), dataMap.getString("id"));
	}

}
