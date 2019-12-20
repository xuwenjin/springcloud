package com.xwj.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xwj.dbdef.ContextUtils;

/**
 * 工厂类
 * 
 * @author XU.WJ 2018年3月13日
 */
@Component
public class FuncFactory {

	@Autowired
	private IQueryFuncA queryFuncA;
	@Autowired
	private IQueryFuncB queryFuncB;
	@Autowired
	private IQueryFuncC queryFuncC;

	public IBaseQuery getQueryFunc(String funcode) {
		IBaseQuery queryFunc = null;
		if ("001".equals(funcode)) {
			queryFunc = ContextUtils.getBean(IQueryFuncA.class);
		} else if ("002".equals(funcode)) {
			queryFunc = ContextUtils.getBean(IQueryFuncB.class);
		} else if ("003".equals(funcode)) {
			queryFunc = ContextUtils.getBean(IQueryFuncC.class);
		}
		return queryFunc;
	}

	public IBaseQuery getQueryFunc2(String funcode) {
		if ("001".equals(funcode)) {
			return queryFuncA;
		} else if ("002".equals(funcode)) {
			return queryFuncB;
		} else if ("003".equals(funcode)) {
			return queryFuncC;
		}
		return null;
	}

}
