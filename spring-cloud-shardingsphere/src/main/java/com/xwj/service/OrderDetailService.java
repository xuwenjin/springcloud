package com.xwj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.entity.OrderInfoDetail;
import com.xwj.repository.OrderDetailRepository;

@Service
public class OrderDetailService {

	@Autowired
	private OrderDetailRepository repository;

	public OrderInfoDetail save(OrderInfoDetail orderDetail) {
		return repository.save(orderDetail);
	}

}
