package com.xwj.service;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AsyncService {

	// 发送提醒短信 1
	@Async
	public void sendMessage1() {
		log.info("发送短信方法---- 1   执行开始");
		try {
			TimeUnit.MILLISECONDS.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("发送短信方法---- 1   执行结束");
	}

	// 发送提醒短信 2
	@Async
	public void sendMessage2() {
		log.info("发送短信方法---- 2   执行开始");
		try {
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("发送短信方法---- 2   执行结束");
	}

}
