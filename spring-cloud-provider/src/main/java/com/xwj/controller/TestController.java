package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.billcode.SequenceGenerator;
import com.xwj.billcode.base.IdWorker;
import com.xwj.properties.TestConfiguration;
import com.xwj.service.test.IBaseService;

@RestController
@RequestMapping("test")
public class TestController {

	@Autowired(required = false)
	private TestConfiguration test1Configuration;
	@Autowired
	private IBaseService baseService;

	@GetMapping("testConfiguration")
	public String testConfiguration() {
		if (test1Configuration != null) {
			return test1Configuration.getField();
		}
		return "OK";
	}

	/**
	 * 雪花算法-测试生成id(1000个线程，每秒可处理请求数：1200左右)
	 */
	@GetMapping("idWorker")
	public String idWorker() throws Exception {
		long nextId = IdWorker.getFlowIdWorkerInstance().nextId();
		// System.out.println("insert into order_info (id) values('" + nextId +
		// "');");
		return nextId + "";
	}

	/**
	 * 生成订单号(1000个线程，每秒可处理请求数：1000左右)
	 * 
	 * 要求：
	 * 1、全球唯一
	 * 2、趋势递增
	 * 3、长度固定
	 * 4、整形，不是字符串
	 * 5、高并发
	 * 6、不能看出订单数量
	 */
	@GetMapping("genOrderNo")
	public String genOrderNo() {
		String phone = "18207136675";
		String orderNo = SequenceGenerator.genOrderNo(phone);
		// System.out.println("insert into order_info (id) values('" + orderNo +
		// "');");
		return orderNo;
	}

	/**
	 * 测试@Conditional相关注解
	 */
	@GetMapping("testConditional")
	public String testConditional() {
		return baseService.sayHello();
	}

}
