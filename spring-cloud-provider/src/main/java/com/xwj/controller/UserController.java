package com.xwj.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

	@OperLogAnn(value = "查询所有", operModule = "人员管理")
	@GetMapping("/findAll")
	public List<UserInfo> findAll() {
		return userService.findAll();
	}

	@OperLogAnn(value = "查询单个", operModule = "人员管理")
	@GetMapping("/find/{id}")
	public UserInfo findById(@PathVariable Long id, HttpServletRequest request) {
		System.out.println("port:" + port);

		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				String value = request.getHeader(name);
				System.out.println(name + ":" + value);
			}
		}

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

}