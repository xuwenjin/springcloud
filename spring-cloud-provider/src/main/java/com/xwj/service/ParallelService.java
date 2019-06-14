package com.xwj.service;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

/**
 * 并行服务
 * 
 * @author xwj
 */
@Service
public class ParallelService {

	public String requestA() {
		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "A";
	}

	public String requestB() {
		try {
			TimeUnit.MILLISECONDS.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "B";
	}

	public String requestC() {
		try {
			TimeUnit.MILLISECONDS.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "C";
	}

}
