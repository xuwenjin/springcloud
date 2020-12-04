package com.xwj.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.listeners.TriggerListenerSupport;

import lombok.extern.slf4j.Slf4j;

/**
 * 触发器监听器
 * 
 * TODO 暂时不知道TriggerListener与JobListener有什么区别
 */
@Slf4j
public class AllTriggerListener extends TriggerListenerSupport {

	@Override
	public String getName() {
		return "allTriggerListener";
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode) {
		log.info("全局触发器监听器：任务执行已完成~~~");
	}

}