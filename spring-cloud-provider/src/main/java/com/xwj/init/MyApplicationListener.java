package com.xwj.init;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 如果容器中有一个ApplicationListener Bean，每当ApplicationContext发布ApplicationEvent时，ApplicationListener Bean将自动被触发(观察者模式)
 * 
 * ContextRefreshedEvent事件(继承ApplicationEvent)：spring容器中所有的Bean初始化后，会发布此事件。通过监听此事件，可以实现自定义处理逻辑(比如服务注册、初始化业务数据等)
 */
@Component
public class MyApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	/**
	 * 监听到事件后，自定义处理
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("MyApplicationListener：容器初始化后，自定义处理逻辑");
	}

}
