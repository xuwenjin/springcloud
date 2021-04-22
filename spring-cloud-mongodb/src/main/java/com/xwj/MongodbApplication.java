package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 测试springboot+Mongodb
 */
//@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class }, scanBasePackages = { "com.xwj.service", "com.xwj.dao" })
//@SpringBootApplication(scanBasePackages = { "com.xwj.service", "com.xwj.dao" })
// @EnableMongoRepositories
//@EnableMongoRepositories(basePackages = { "com.xwj.dao" })
@SpringBootApplication
public class MongodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongodbApplication.class, args);
	}

}
