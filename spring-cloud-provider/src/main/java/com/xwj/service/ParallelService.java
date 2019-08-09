package com.xwj.service;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 并行服务
 * 
 * @author xwj
 */
@Slf4j
@Service
public class ParallelService {

	public String requestA() {
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			log.info("requestA被打断");
		}
		return "A";
	}

	public String requestB() {
		try {
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (InterruptedException e) {
			log.info("requestB被打断");
		}
		return "B";
	}

	public String requestC() {
		try {
			TimeUnit.MILLISECONDS.sleep(10000);
		} catch (InterruptedException e) {
			log.info("requestC被打断");
		}
		return "C";
	}

}
