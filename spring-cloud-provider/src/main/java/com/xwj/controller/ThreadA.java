package com.xwj.controller;

import com.xwj.service.RedisService;

public class ThreadA extends Thread {

	private RedisService service;

	public ThreadA(RedisService service) {
		this.service = service;
	}

	@Override
	public void run() {
		service.seckill();
	}
}
