package com.xwj.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.xwj.service.spring.MyService;

/**
 * FactoryBean：工厂类Bean接口
 * 
 * 实现FactoryBean接口，可以让我们自定义的Bean注入到Spring容器中
 * 
 * @author xuwenjin 2021年2月28日
 */
@Component
public class XwjFactoryBean implements FactoryBean<MyService> {

	/**
	 * 返回由FactoryBean创建的bean实例，如果isSingleton()返回true，则该实例会放到Spring容器中单实例缓存池中
	 */
	@Override
	public MyService getObject() throws Exception {
		System.out.println("自定义bean~~~");
		// 自定义bean。这里还可以返回MyService的代理类，或者自己添加MyService属性等
		return new MyService();
	}

	/**
	 * 返回FactoryBean创建的bean类型
	 */
	@Override
	public Class<?> getObjectType() {
		return MyService.class;
	}

	/**
	 * 创建的bean是否单例(默认true)
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
