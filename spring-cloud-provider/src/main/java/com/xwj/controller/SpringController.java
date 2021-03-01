package com.xwj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.service.spring.MyService;
import com.xwj.service.spring.MyService2;
import com.xwj.service.spring.MyService3;
import com.xwj.spring.ContextUtils;
import com.xwj.spring.XwjBeanFactoryPostProcessor;
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
		System.out.println(ContextUtils.getApplicationContext().getBeansOfType(MyService.class));
		System.out.println(ContextUtils.getApplicationContext().getBeansOfType(XwjFactoryBean.class));

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

	/**
	 * 测试BeanFactory
	 */
	@GetMapping("/bf")
	public String testBeanFactory() {
		System.out.println(ContextUtils.getBean("xwjBeanFactoryPostProcessor")); // 单例对象
		System.out.println(ContextUtils.getBean("myService2")); // 单例对象

		System.out.println(ContextUtils.getBean(MyService2.class));
		System.out.println(ContextUtils.getBean(XwjBeanFactoryPostProcessor.class));

		MyService2 myService2 = ContextUtils.getBean(MyService2.class);
		return myService2.eat("abc");
	}

	/**
	 * 测试@Import
	 */
	@GetMapping("/testImport")
	public String testImport() {
		System.out.println(ContextUtils.getApplicationContext().getBeansOfType(MyService3.class));

		System.out.println(ContextUtils.getBean(MyService3.class));

		// beanName为 com.xwj.service.spring.MyService3 的，对应的bean为MyService3
		System.out.println(ContextUtils.getBean("com.xwj.service.spring.MyService3")); // 单例对象

		MyService3 myService3 = ContextUtils.getBean(MyService3.class);
		return myService3.eat("abc");
	}

}
