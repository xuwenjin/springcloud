package com.xwj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.entity.OrderInfo;
import com.xwj.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;

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

}
