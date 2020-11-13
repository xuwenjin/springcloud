package com.xwj.init;

import java.io.IOException;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ContextListener {

	private static int counter;

	/**
	 * 监听所有事件
	 * 
	 * spring容器发布事件顺序：
	 * 
	 * 1、InstanceRegisteredEvent
	 * 
	 * 2、ParentContextAvailableEvent
	 * 
	 * 3、ApplicationPreparedEvent
	 * 
	 * 4、ContextRefreshedEvent
	 * 
	 * 5、ServletWebServerInitializedEvent
	 * 
	 * 6、ApplicationStartedEvent
	 * 
	 * 7、ApplicationReadyEvent
	 */
	@EventListener
	public void handle(Object event) throws IOException {
		System.out.println("*********" + (++counter) + ": " + event.toString() + "*********");
	}

}