package com.xwj.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

/**
 * 测试Sentinel功能
 * 
 * 默认Sentinel日志文件路径：C:\Users\xwj\logs\csp，文件如：service-provider-metrics.log.2020-12-09
 * 
 */
@RestController
@RequestMapping("sen")
public class SentinelController {

	/**资源名称*/
	private final String RESOURCE_HELLO = "res_hello";
	private final String RESOURCE_ANN = "res_ann";

	/**
	 * 初始化流量控制规则
	 */
	@PostConstruct // 当前类的构造函数执行之后执行
	public void initFlowRules() {
		System.out.println("SentinelController.initFlowRules：初始化流量控制规则");
		List<FlowRule> rules = new ArrayList<>();

		// 1、创建流量控制规则
		FlowRule rule = new FlowRule();
		rule.setResource(RESOURCE_HELLO);// 设置资源名称
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS); // 设置流量控制规则类型(主要有两种统计类型：统计线程数、统计QPS(默认))
		rule.setCount(3); // 设置QPS每秒通过的请求个数
		rules.add(rule);

		// 2、加载流量控制规则
		FlowRuleManager.loadRules(rules);
	}

	/**
	 * 初始化熔断降级规则
	 */
	@PostConstruct // 当前类的构造函数执行之后执行
	public void initDegradeRules() {
		System.out.println("SentinelController.initDegradeRules：初始化熔断降级规则");
		List<DegradeRule> rules = new ArrayList<>();

		// 1、创建流量控制规则
		DegradeRule rule = new DegradeRule();
		rule.setResource(RESOURCE_ANN);// 设置资源名称
		rule.setGrade(RuleConstant.DEGRADE_GRADE_RT); // 设置熔断策略(主要有三种统计类型：慢调用比例(平均响应时间)、异常比例、异常数策略)
		rule.setCount(0.001); // 设置阈值(慢调用比例模式下为慢调用临界RT(超出该值计为慢调用)；异常比例/异常数模式下为对应的阈值)
		rule.setTimeWindow(10); // 设置熔断时长(单位：秒)
		rules.add(rule);

		// 2、加载熔断降级规则
		DegradeRuleManager.loadRules(rules);
	}

	/**
	* 使用自定义流量控制规则(抛出异常的方式定义资源)
	*/
	@GetMapping("/hello")
	public String hello() {
		// 使用SphU.entry方式：当资源发生了限流之后会抛出 BlockException
		try (Entry entry = SphU.entry(RESOURCE_HELLO)) { // try-with-resources
			// 被保护的业务逻辑
			return "Hello World！";
		} catch (BlockException ex) {
			// 资源访问阻止，被限流或被降级
			return "系统繁忙，请稍候...";
		}
	}

	/**
	* 使用自定义流量控制规则(使用布尔值方式定义资源)
	*/
	@GetMapping("/hello2")
	public String hello2() {
		// 使用SphO.entry方式：当资源发生了限流之后会返回 false，这个时候可以根据返回值，进行限流之后的逻辑处理
		if (SphO.entry(RESOURCE_HELLO)) {
			try {
				// 被保护的业务逻辑
				return "Hello World2！";
			} finally {
				// 务必保证finally会被执行
				// SphO.entry需要与SphO.exit成对出现，否则会导致调用链记录异常，抛出ErrorEntryFreeException异常
				SphO.exit();
			}
		} else {
			// 资源访问阻止，被限流或被降级
			return "系统繁忙，请稍候...";
		}
	}

	/**
	* 基于@SentinelResource注解，测试Sentinel限流
	* 
	* 注解@SentinelResource属性：
	* 
	* value：资源名称，必需项
	* blockHandler：处理 BlockException 的函数名称
	* fallback：用于在抛出异常的时候提供 fallback 处理逻辑
	*/
	@SentinelResource(value = RESOURCE_ANN, blockHandler = "exceptionHandler", fallback = "fallbackHandler")
	@GetMapping("/ann")
	public String helloAnn(String id) {
		return "ann";
	}

	/**
	* 处理 BlockException 的方法名，可选项。若未配置，则将 BlockException 直接抛出
	*
	* 1、blockHandler函数访问范围需要是public
	* 2、返回类型需要与原方法相匹配参数类型需要和原方法相匹配并且最后加一个额外的参数，类型为BlockException
	* 3、blockHandler函数默认需要和原方法在同一个类中
	*/
	public String exceptionHandler(String id, BlockException e) {
		System.out.println("exceptionHandler：" + id);
		return "系统繁忙，请稍候...";
	}

	/**
	 * 类似于上面的exceptionHandler，只是可以处理任务异常
	 */
	public String fallbackHandler(String id) {
		System.out.println("fallbackHandler：" + id);
		return "系统繁忙，请稍候...";
	}

}