package com.xwj.factory;

import org.springframework.stereotype.Service;

/**
 * 子类C
 * 
 * @author XU.WJ 2019年12月20日
 */
@Service
public class IQueryFuncC implements IBaseQuery {

	@Override
	public void print() {
		System.out.println("我是子类C");
	}

}
