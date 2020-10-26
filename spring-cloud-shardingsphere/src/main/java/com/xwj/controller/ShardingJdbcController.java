package com.xwj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.entity.OrderInfo;
import com.xwj.service.OrderService;

/**
 * 分库分表
 */
@RestController
@RequestMapping("shardingjdbc")
public class ShardingJdbcController {

	@Autowired
	private OrderService orderService;

	/**
	 * 保存
	 */
	@PostMapping("/order/save")
	public OrderInfo save(@RequestBody OrderInfo order) {
		return orderService.save(order);
	}

	/**
	 * 查询详情
	 */
	@GetMapping("findById/{id}")
	public OrderInfo findById(@PathVariable String id) {
		return orderService.findById(Long.valueOf(id));
	}

	/**
	 * 查询列表
	 */
	@GetMapping("/order/findAll")
	public List<OrderInfo> findUserAll() {
		return orderService.findAll();
	}

}
