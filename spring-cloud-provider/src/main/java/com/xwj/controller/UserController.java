package com.xwj.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.xwj.entity.UserInfo;
import com.xwj.operlog.MyLog;
import com.xwj.service.IUserService;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private IUserService userService;

	@MyLog("查询")
	@GetMapping("/findAll")
	public List<UserInfo> findAll() {
		return userService.findAll();
	}

	@MyLog("查询")
	@GetMapping("/find/{id}")
	public UserInfo findById(@PathVariable Long id) {
		return userService.findById(id);
	}

	@PostMapping("save")
	public UserInfo save(@RequestBody UserInfo user) {
		return userService.save(user);
	}

	@GetMapping("saveAll")
	public void saveAll() {
		List<UserInfo> list = new ArrayList<>();
		for (int i = 11; i < 10000; i++) {
			UserInfo user = new UserInfo();
			user.setAge(i);
			user.setEmail(i + "@qq.com");
			list.add(user);
		}
		userService.saveAll(list);
	}

	@GetMapping("/findName")
	public String findName(@RequestParam String name) {
		return name;
	}

	/**
	 * 测试Sentinel限流
	 */
	@SentinelResource(value = "all", blockHandler = "exceptionHandler")
	@GetMapping("/all")
	public List<UserInfo> all(String id) {
		return userService.findAll();
	}

	/**
	 * 处理 BlockException 的方法名，可选项。若未配置，则将 BlockException 直接抛出
	 * 
	 * 1、blockHandler函数访问范围需要是public
	 * 2、返回类型需要与原方法相匹配参数类型需要和原方法相匹配并且最后加一个额外的参数，类型为BlockException
	 * 3、blockHandler函数默认需要和原方法在同一个类中
	 */
	public String exceptionHandler(String id, BlockException e) {
		e.printStackTrace();
		return "错误发生在" + id;
	}

}