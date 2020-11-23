package com.xwj;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xwj.entity.OrderInfo;
import com.xwj.service.OrderService;

/**
 * 测试查询
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleTest {

	@Autowired
	private OrderService orderService;

	@Test
	public void testInsert() {
		OrderInfo order = new OrderInfo();
		order.setOrderType(0);
		order.setStatus("created");
		orderService.save(order);
	}

	@Test
	public void testSelect() {
		Long id = 0L;
		OrderInfo order = orderService.findById(id);
		System.out.println(order);
	}

	@Test
	public void testSelect2() {
		List<OrderInfo> list = orderService.findAll();
		list.forEach(d -> System.out.println(d));
	}

}