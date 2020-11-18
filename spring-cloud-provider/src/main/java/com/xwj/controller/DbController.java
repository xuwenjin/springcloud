package com.xwj.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

	/** 主数据源 */
	@Resource(name = "primaryJdbcTemplate")
	protected JdbcTemplate primaryJdbcTemplate;

	/** 次数据源 */
	@Resource(name = "secondaryJdbcTemplate")
	protected JdbcTemplate secondaryJdbcTemplate;

	@GetMapping("comment")
	public String comment() {
		service.createComment();
		return "ok";
	}

	/**
	 * 测试多数据源
	 */
	@GetMapping("mutiDb")
	public String mutiDb() {
		// 往第一个数据源中插入 2 条数据
		primaryJdbcTemplate.update("insert into user_info(name,age) values(?, ?)", "aaa", 20);
		primaryJdbcTemplate.update("insert into user_info(name,age) values(?, ?)", "bbb", 30);

		// 往第二个数据源中插入 1 条数据，若插入的是第一个数据源，则会主键冲突报错
		secondaryJdbcTemplate.update("insert into user_info(name,age) values(?, ?)", "ccc", 20);

		// 查一下第一个数据源中是否有 2 条数据，验证插入是否成功
		System.out.println(primaryJdbcTemplate.queryForObject("select count(1) from user_info", String.class));

		// 查一下第一个数据源中是否有 1 条数据，验证插入是否成功
		System.out.println(secondaryJdbcTemplate.queryForObject("select count(1) from user_info", String.class));

		return "ok";
	}

}
