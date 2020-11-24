package com.xwj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.dao.OrderDao;
import com.xwj.entity.OrderInfo;
import com.xwj.repository.OrderRepository;
import com.xwj.vo.OrderDetailVo;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;
	@Autowired
	private OrderDao orderDao;

	public OrderInfo findById(Long id) {
		Optional<OrderInfo> optional = repository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public List<OrderInfo> findAll() {
		return repository.findAll();
	}

	public OrderInfo save(OrderInfo order) {
		return repository.save(order);
	}

	/**
	 * 连表查询订单和订单详情(分页查询)
	 */
	public List<OrderDetailVo> queryOrderDetailList(int page, int pageSize) {
		int start = (page - 1) * pageSize;
		return orderDao.queryOrderDetailList(start, pageSize);
	}

}
