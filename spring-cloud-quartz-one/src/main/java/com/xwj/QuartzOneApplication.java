package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 测试quartz(springboot 2.x配合org.quartz-scheduler依赖)
 */
@SpringBootApplication
public class QuartzOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuartzOneApplication.class, args);
	}

}
