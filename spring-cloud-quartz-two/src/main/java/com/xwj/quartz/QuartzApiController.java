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

import com.xwj.quartz.job.MyJob1;
import com.xwj.quartz.job.MyJob2;
import com.xwj.quartz.job.MyJob3;
import com.xwj.quartz.listener.AllJobListener;
import com.xwj.quartz.listener.MyJob3Listener;
import com.xwj.quartz.listener.AllTriggerListener;
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
	@GetMapping("/job/start/{id}")
	public void startQuartzJob(@PathVariable String id) {
		// 20s之后执行
		LocalDateTime ldt = LocalDateTime.now();
		Date date = TimeUtil.localDateTime2Date(ldt.plusSeconds(20));

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		quartzScheduler.addJob(MyJob1.class, id, date, params);
	}

	/**
	 * 带定时表达式的任务
	 */
	@GetMapping("/job/cron/{id}")
	public void cronQuartzJob(@PathVariable String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		// 每10秒执行一次
		quartzScheduler.addJobWithCron(MyJob2.class, id, "0/10 * * * * ?", params);
	}

	/**
	 * 带定时表达式和指定时间范围的任务
	 */
	@GetMapping("/job/cronInRange/{id}")
	public void cronQuartzJobInRange(@PathVariable String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		// 每10秒执行一次
		quartzScheduler.addJobWithCron(MyJob3.class, id, "0/10 * * * * ?", 30, params);
		// 增加任务监听
		quartzScheduler.addJobListener(id, MyJob3.class, new MyJob3Listener(quartzScheduler));
	}

	/**
	 * 删除某个任务
	 */
	@GetMapping("/job/delete")
	public boolean deleteJob(String name, String group) {
		return quartzScheduler.deleteJob(name, group);
	}

	/**
	 * 修改任务执行时间
	 */
	@GetMapping("/job/modify")
	public boolean modifyQuartzJob(String name, String group, String time) {
		return quartzScheduler.modifyJob(name, group, time);
	}

	/**
	 * 暂停某个任务
	 */
	@GetMapping("/job/pause")
	public void pauseQuartzJob(String name, String group) {
		quartzScheduler.pauseJob(name, group);
	}

	/**
	 * 暂停所有任务
	 */
	@GetMapping("/job/pauseAll")
	public void pauseAllQuartzJob() {
		quartzScheduler.pauseAllJob();
	}

	/**
	 * 注册任务监听器
	 */
	@GetMapping("/addJobListener")
	public void addJobListener() {
		quartzScheduler.addJobListener(new AllJobListener());
	}

	/**
	 * 注册触发器监听器
	 */
	@GetMapping("/addTriggerListener")
	public void addTriggerListener() {
		quartzScheduler.addTriggerListener(new AllTriggerListener());
	}

}
