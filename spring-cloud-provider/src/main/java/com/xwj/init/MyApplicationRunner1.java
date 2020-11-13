package com.xwj.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化类
 * 
 * 查看SpringBoot的启动过程(SpringApplication.run()方法)，会发现在spring容器启动完成后，会调用afterRefresh()方法，该方法中会
 * 执行加入到spring容器中并实现了ApplicationRunner的Bean
 * 
 */
@Order(1) // @Order注解可以改变执行顺序，越小越先执行
@Component
public class MyApplicationRunner1 implements ApplicationRunner {

	/**
	 * 会在服务启动完成后立即执行
	 */
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		System.out.println("MyApplicationRunner1----" + arg0);
	}

}
