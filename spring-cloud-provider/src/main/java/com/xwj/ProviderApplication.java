package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
public class ProviderApplication {

	public static void main(String[] args) {
//		initFlowRules();
		SpringApplication.run(ProviderApplication.class, args);
	}
	
//	public static void initFlowRules() {
//		List<FlowRule> rules = new ArrayList<>();
//		FlowRule rule = new FlowRule();
//		rule.setResource("all");
//		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//		rule.setCount(1);
//		rules.add(rule);
//		FlowRuleManager.loadRules(rules);
//	}

}
