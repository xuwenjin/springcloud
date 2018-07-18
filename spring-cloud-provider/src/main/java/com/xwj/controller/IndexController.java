package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.dbdef.CommentService;
import com.xwj.entity.User;
import com.xwj.service.IUserService;

@RestController
public class IndexController {

	@Autowired
	private IUserService userService;

	@Autowired
	private CommentService service;

	@GetMapping("/find/{id}")
	public User findById(@PathVariable Long id) {
		return userService.findById(id);
	}
	
	@GetMapping("/findName")
	public String findName(@RequestParam String name) {
		return name;
	}

	/**
	 * 生成表备注
	 * 
	 * @return
	 */
	@GetMapping("/comment")
	public String createComment() {
		service.createComment();
		return "ok";
	}

}