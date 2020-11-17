package com.xwj.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.xwj.annotations.OperLogAnn;
import com.xwj.entity.UserInfo;
import com.xwj.service.IUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

	@Value("${server.port}")
	private String port;

	@Autowired
	private IUserService userService;

	// @OperLogAnn(value = "查询所有", operModule = "人员管理")
	@GetMapping("/findAll")
	public List<UserInfo> findAll() {
		return userService.findAll();
	}

	@OperLogAnn(value = "查询单个", operModule = "人员管理")
	@GetMapping("/find/{id}")
	public UserInfo findById(@PathVariable Long id) {
		System.out.println("port:" + port);
		return userService.findById(id);
	}

	@OperLogAnn(value = "新增人员", operModule = "人员管理")
	@PostMapping("save")
	public UserInfo save(@RequestBody UserInfo user) {
		return userService.save(user);
	}

	/**
	 * 测试乐观锁
	 */
	@PostMapping("testOptimismLock")
	public void testOptimismLock() {
		List<UserInfo> list = new ArrayList<>();

		UserInfo user1 = userService.findById(100L);
		user1.setName("1111");
		list.add(user1);

		UserInfo user2 = userService.findById(101L);
		user2.setName("2222");
		list.add(user2);

		for (UserInfo user : list) {
			try {
				userService.save(user);
			} catch (ObjectOptimisticLockingFailureException e) {
				log.error("保存异常：{}", e.getMessage());
				// log.error("保存异常", e);
			}
		}

		// 当批量保存时，如果出现一条数据version不一致，则会报乐观锁，导致整个事物回滚
		// userService.saveAll(list);
	}

	@GetMapping("saveAll")
	public void saveAll() {
		List<UserInfo> list = new ArrayList<>();
		for (int i = 1; i < 100; i++) {
			UserInfo user = new UserInfo();
			user.setAge(i);
			user.setEmail(i + "@qq.com");
			user.setUsername("abc" + i);
			user.setPassword("123456");
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