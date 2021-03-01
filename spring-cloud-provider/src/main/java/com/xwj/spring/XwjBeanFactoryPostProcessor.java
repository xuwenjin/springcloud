package com.xwj.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import com.xwj.service.spring.MyService2;

/**
 * BeanFactory的后置处理器
 * 
 * @author xuwenjin 2021年3月1日
 */
@Component
public class XwjBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	/**
	 * 将MyService2使用单例模式注册到BeanFactory
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("XwjBeanFactoryPostProcessor.postProcessBeanFactory");
		beanFactory.registerSingleton("myService2", new MyService2());
	}

}
