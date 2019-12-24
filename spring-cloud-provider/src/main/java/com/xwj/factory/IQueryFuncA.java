package com.xwj.factory;

import org.springframework.stereotype.Service;

/**
 * 子类A
 * 
 * @author XU.WJ 2019年12月20日
 */
@Service("001")
public class IQueryFuncA implements IBaseQuery {

	@Override
	public void print() {
		System.out.println("我是子类A");
	}

}
