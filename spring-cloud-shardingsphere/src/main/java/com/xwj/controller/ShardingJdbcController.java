package com.xwj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.entity.AddressInfo;
import com.xwj.entity.OrderInfo;
import com.xwj.service.AddressService;
import com.xwj.service.UserService;

/**
 * 分库分表
 */
@RestController
@RequestMapping("shardingjdbc")
public class ShardingJdbcController {

	@Autowired
	private UserService userService;
	@Autowired
	private AddressService addressService;

	/**
	 * 保存
	 */
	@PostMapping("/user/save")
	public OrderInfo save(@RequestBody OrderInfo user) {
		return userService.save(user);
	}

	/**
	 * 保存
	 */
	@PostMapping("/address/save")
	public AddressInfo save(@RequestBody AddressInfo address) {
		return addressService.save(address);
	}

	/**
	 * 查询详情
	 */
	@GetMapping("findById/{id}")
	public OrderInfo findById(@PathVariable String id) {
		return userService.findById(Long.valueOf(id));
	}

	/**
	 * 查询列表
	 */
	@GetMapping("/user/findAll")
	public List<OrderInfo> findUserAll() {
		return userService.findAll();
	}

	/**
	 * 查询列表
	 */
	@GetMapping("/address/findAll")
	public List<AddressInfo> findAddressAll() {
		return addressService.findAll();
	}

}
