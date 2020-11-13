package com.xwj.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化类
 * 
 * 查看SpringBoot的启动过程(SpringApplication.run()方法)，会发现在spring容器启动完成后，会调用afterRefresh()方法，该方法中会
 * 执行加入到spring容器中，并实现了CommandLineRunner的Bean
 * 
 */
@Order(1) // @Order注解可以改变执行顺序，越小越先执行
@Component
public class MyCommandLineRunner1 implements CommandLineRunner {

	/**
	 * 会在服务启动完成后立即执行
	 */
	@Override
	public void run(String... args) throws Exception {
		System.out.println("MyCommandLineRunner1----" + args);
	}

}
