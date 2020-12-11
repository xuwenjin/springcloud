package com.xwj.quartz;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.quartz.job.MyJob;
import com.xwj.util.TimeUtil;

/**
 * quartz测试类
 */
@RestController
@RequestMapping("/quartz")
public class QuartzApiController {

	@Autowired
	private MyQuartzScheduler quartzScheduler;

	/**
	 * 指定时间点触发的任务
	 */
	@RequestMapping("/job/start/{id}")
	public void startQuartzJob(@PathVariable String id) {
		// 20s之后执行
		LocalDateTime ldt = LocalDateTime.now();
		Date date = TimeUtil.localDateTime2Date(ldt.plusSeconds(20));

		// 执行一次
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		quartzScheduler.addJob(MyJob.class, id, date, params);
	}

	/**
	 * 定时任务
	 */
	@RequestMapping("/job/cron/{id}")
	public void cronQuartzJob(@PathVariable String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		// 每10秒执行一次
		quartzScheduler.addJobWithCron(MyJob.class, id, "0/10 * * * * ?", params);
	}

	/**
	 * 带定时表达式和指定时间范围的任务
	 */
	@GetMapping("/job/cronInRange/{id}")
	public void cronQuartzJobInRange(@PathVariable String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);

		// 每10秒执行一次，执行30秒钟
		quartzScheduler.addJobWithCron(MyJob.class, id, "0/10 * * * * ?", 30, params);
	}

	/**
	 * 删除某个任务
	 */
	@RequestMapping(value = "/job/delete")
	public boolean deleteJob(String name, String group) {
		return quartzScheduler.deleteJob(name, group);
	}

	/**
	 * 修改任务执行时间
	 */
	@RequestMapping("/job/modify")
	public boolean modifyQuartzJob(String name, String group, String time) {
		return quartzScheduler.modifyJob(name, group, time);
	}

	/**
	 * 暂停某个任务
	 */
	@RequestMapping(value = "/job/pause")
	public void pauseQuartzJob(String name, String group) {
		quartzScheduler.pauseJob(name, group);
	}

	/**
	 * 暂停所有任务
	 */
	@RequestMapping(value = "/job/pauseAll")
	public void pauseAllQuartzJob() {
		quartzScheduler.pauseAllJob();
	}

}
