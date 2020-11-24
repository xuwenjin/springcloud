package com.xwj;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xwj.entity.OrderInfo;
import com.xwj.entity.OrderInfoDetail;
import com.xwj.service.OrderDetailService;
import com.xwj.service.OrderService;
import com.xwj.vo.OrderDetailVo;

/**
 * 测试订单表
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;

	/**
	 * 保存订单
	 */
	@Test
	public void testInsert() {
		for (int i = 1; i <= 20; i++) {
			OrderInfo order = new OrderInfo();
			order.setId(Long.valueOf(i));
			order.setStatus("created");

			// 随机生成0和1(分库时，用的orderType分片，这里可随机插入到不同的库)
			int orderType = (int) (10 * Math.random()) % 2;
			order.setOrderType(orderType);

			orderService.save(order);
		}
	}

	/**
	 * 查询订单，然后保存订单详情信息
	 */
	@Test
	public void testFindInsert() {
		List<OrderInfo> orderList = orderService.findAll();
		int orderCount = orderList.size();
		System.out.println("订单数量：" + orderCount);

		for (int i = 0; i < orderCount; i++) {
			OrderInfo order = orderList.get(i);

			OrderInfoDetail orderDetail = new OrderInfoDetail();
			orderDetail.setId(Long.valueOf(i + 1));
			orderDetail.setOrderId(order.getId());
			orderDetail.setOrderType(order.getOrderType());
			orderDetail.setDescription("不错的商品" + (i + 1));
			orderDetailService.save(orderDetail);
		}
	}

	@Test
	public void testSelect() {
		Long id = 0L;
		OrderInfo order = orderService.findById(id);
		System.out.println(order);
	}

	@Test
	public void testSelectAll() {
		List<OrderInfo> orderList = orderService.findAll();
		orderList.forEach(d -> System.out.println(d));
	}

	/**
	 * 连表查询(分页查询)
	 */
	@Test
	public void testSelectPage() {
		List<OrderDetailVo> orderList = orderService.queryOrderDetailList(3, 5);
		System.out.println(orderList.size());
	}

	/**
	 * 总数统计
	 */
	@Test
	public void testSelectCount() {
		long count = orderService.queryCount();
		System.out.println(count);
	}

	/**
	 * 分组统计
	 */
	@Test
	public void testSelectGroup() {
		List<Map<String, Object>> list = orderService.queryGroupList();
		System.out.println(list);
	}

}