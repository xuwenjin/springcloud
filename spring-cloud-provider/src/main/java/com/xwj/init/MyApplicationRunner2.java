package com.xwj.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化类
 */
@Order(2)
@Component
public class MyApplicationRunner2 implements ApplicationRunner {

	/**
	 * 会在服务启动完成后立即执行
	 */
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		System.out.println("MyApplicationRunner2----" + arg0);
	}

}
