package com.xwj.dbdef;
//package com.xwj.common;
//
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ApplicationContextEvent;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CommentListenner implements ApplicationListener<ApplicationContextEvent> {
//
//	@Value("${refer.init:N}")
//	private String referInit;
//	
//	@Autowired
//	private CommentService service;
//
//	@Override
//	public void onApplicationEvent(ApplicationContextEvent event) {
//		Map<String, Object> beanMap = ContextUtils.getApplicationContext().getBeansWithAnnotation(Comment.class);
//		for (String key : beanMap.keySet()) {
//			Object bean = beanMap.get(key);
//			service.createTableComment(bean.getClass());
//			service.createColumnCommnet(bean.getClass());
//		}
//	}
//
//}
