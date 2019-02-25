package com.xwj.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化类
 */
@Order(2)
@Component
public class MyCommandLineRunner2 implements CommandLineRunner {

	/**
	 * 会在服务启动完成后立即执行
	 */
	@Override
	public void run(String... args) throws Exception {
		System.out.println("MyCommandLineRunner2----" + args);
	}

}
