package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.dbdef.CommentService;

/**
 * 添加数据库表及列备注
 * 
 * @author xuwenjin 2018年7月14日
 */
@RestController
@RequestMapping("db")
public class DbController {

	@Autowired
	private CommentService service;

	@GetMapping("comment")
	public String comment() {
		service.createComment();
		return "ok";
	}

}
