package com.xwj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.MyService;
import com.xwj.spring.ContextUtils;
import com.xwj.spring.XwjFactoryBean;

/**
 * spring相关
 */
@RestController
@RequestMapping("myspring")
public class SpringController {

	/**
	 * 测试FactoryBean
	 * 
	 * 调用getBean(beanName)时，实际上是调用XwjFactoryBean.getObject()方法
	 * 
	 * 注入到spring容器后，会生成两个bean，如下所示：
	 * {xwjFactoryBean=com.xwj.service.MyService@70907457}
	 * {&xwjFactoryBean=com.xwj.spring.XwjFactoryBean@6f61698}
	 * 
	 */
	@GetMapping("/fb")
	public String testFactoryBean() {
		// beanName为 xwjFactoryBean 的，对应的bean为MyService
		// beanName为 &xwjFactoryBean 的，对应的bean为XwjFactoryBean
		System.out.println(ContextUtils.getBean("xwjFactoryBean")); // 单例对象
		System.out.println(ContextUtils.getBean("xwjFactoryBean")); // 单例对象
		System.out.println(ContextUtils.getBean("&xwjFactoryBean"));

		System.out.println(ContextUtils.getBean(MyService.class));
		System.out.println(ContextUtils.getBean(XwjFactoryBean.class));

		MyService myService = ContextUtils.getBean(MyService.class);
		return myService.eat("abc");
	}

}
