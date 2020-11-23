package com.xwj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xwj.entity.SysDict;
import com.xwj.service.DictService;

/**
 * 测试广播表
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DictTest {

	@Autowired
	private DictService dictService;

	@Test
	public void testInsert() {
		SysDict order = new SysDict();
		order.setId(1L);
		order.setCode("created");
		order.setValue("新建");
		dictService.save(order);
	}

	@Test
	public void testSelect() {
		Long id = 1L;
		SysDict order = dictService.findById(id);
		System.out.println(order);
	}

	@Test
	public void testDelete() {
		Long id = 1L;
		dictService.delete(id);
	}

}
