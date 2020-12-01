package com.xwj.service.test;

public class LocalServiceImpl implements IBaseService {

	@Override
	public String sayHello() {
		return "Hello LocalService";
	}

	@Override
	public String getScheme() {
		return "local";
	}

}
