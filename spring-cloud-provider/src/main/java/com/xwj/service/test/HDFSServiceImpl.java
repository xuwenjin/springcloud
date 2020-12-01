package com.xwj.service.test;

public class HDFSServiceImpl implements IBaseService {

	@Override
	public String sayHello() {
		return "Hello HDFSService";
	}

	@Override
	public String getScheme() {
		return "hdfs";
	}

}
