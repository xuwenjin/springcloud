package com.xwj.factory;

import org.springframework.stereotype.Service;

/**
 * 子类B
 * 
 * @author XU.WJ 2019年12月20日
 */
@Service("002")
public class IQueryFuncB implements IBaseQuery {

	@Override
	public void print() {
		System.out.println("我是子类B");
	}

}
