package com.xwj.init;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 注解@EventListener跟实现ApplicationListener效果是一样的，就是监听ApplicationEvent事件
 */
@Component
public class MyEventListener {

	@EventListener
	public void handleContextRefreshedEvent(ContextRefreshedEvent event) {
		System.out.println("监听ContextRefreshedEvent事件(使用@EventListener注解)");
	}

	@EventListener
	public void handleContextRefreshedEvent(ServletWebServerInitializedEvent event) {
		System.out.println("监听ServletWebServerInitializedEvent事件(使用@EventListener注解)");
	}

}
