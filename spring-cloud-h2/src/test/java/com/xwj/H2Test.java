package com.xwj;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xwj.entity.XwjUser;
import com.xwj.service.UserService;

/**
 * 测试
 * 
 * 由于H2是关系内存数据库，当程序启动的时候，会在内存中创建表，并将数据存储在内存中，当重启程序后，会自动删除内存中的数据
 * 从而可以很好的用来做dao层的单元测试和service层的单元测试，使整个程序不会依赖具体的数据库，同时也提高了单元测试的效率。
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class H2Test {

	@Autowired
	private UserService userService;

	/**
	 * 测试新增+查询
	 */
	@Test
	public void testFind() {
		List<XwjUser> list = userService.findAll();
		System.out.println("list: " + list);
	}

	/**
	 * 测试新增+查询
	 */
	@Test
	public void testInsertAndFind() {
		// 1、新增
		XwjUser user = new XwjUser();
		user.setAge(19);
		user.setName("张三");
		user.setCreateDate(new Date());
		XwjUser newUser = userService.save(user);

		// 2、查询
		Long id = newUser.getId();
		System.out.println("findById: " + userService.findById(id));
	}

}
