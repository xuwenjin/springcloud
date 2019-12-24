package com.xwj.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FactoryForStrategy {

	/**
	 * AutoWired一个Map<String, IBaseQuery>,这个会在初始化的时候将所有的IBaseQuery自动加载到Map中
	 * 
	 * key为注入后bean的名字
	 * 
	 * value为实例化后的bean
	 */
	@Autowired
	private Map<String, IBaseQuery> strategys = new ConcurrentHashMap<>(3);

	public IBaseQuery getStrategy(String funcode) {
		IBaseQuery strategy = strategys.get(funcode);
		if (strategy == null) {
			throw new RuntimeException("no strategy defined");
		}
		return strategy;
	}

}