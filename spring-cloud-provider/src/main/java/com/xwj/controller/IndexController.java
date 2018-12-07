package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.entity.User;
import com.xwj.operlog.MyLog;
import com.xwj.service.IUserService;

@RestController
@RequestMapping("index")
public class IndexController {

	@Autowired
	private IUserService userService;

	@MyLog("查询")
	@GetMapping("/find/{id}")
	public User findById(@PathVariable Long id) {
		return userService.findById(id);
	}

	@PostMapping("post")
	public String post(@RequestBody User user) {
		return user.getId() + "";
	}

	@GetMapping("/findName")
	public String findName(@RequestParam String name) {
		return name;
	}

}