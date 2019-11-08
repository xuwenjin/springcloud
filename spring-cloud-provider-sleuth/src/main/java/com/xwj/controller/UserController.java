package com.xwj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.entity.MyUser;
import com.xwj.service.IUserService;

@RestController
@RequestMapping("myuser")
public class UserController {

	@Autowired
	private IUserService userService;

	@GetMapping("/findAll")
	public List<MyUser> findAll() {
		return userService.findAll();
	}

	@GetMapping("/find/{id}")
	public MyUser findById(@PathVariable Long id) {
		return userService.findById(id);
	}

	@GetMapping("save")
	public MyUser save() {
		MyUser user = new MyUser();
		user.setUsername("xuwenjin");
		user.setAge(20);
		return userService.save(user);
	}

}