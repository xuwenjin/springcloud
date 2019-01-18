package com.xwj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.datasource.service.User2Service;
import com.xwj.entity.User;
import com.xwj.operlog.MyLog;
import com.xwj.service.IUserService;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private User2Service user2Service;
	
	@RequestMapping("/insert2")
	public void insertUser2(){
		user2Service.addDoc();
	}
	
	@GetMapping("/findAll")
	public List<User> findAll() {
		return userService.findAll();
	}

	@MyLog("查询")
	@GetMapping("/find/{id}")
	public User findById(@PathVariable Long id) {
		return userService.findById(id);
	}

	@PostMapping("save")
	public User save(@RequestBody User user) {
		return userService.save(user);
	}

	@GetMapping("/findName")
	public String findName(@RequestParam String name) {
		return name;
	}

}