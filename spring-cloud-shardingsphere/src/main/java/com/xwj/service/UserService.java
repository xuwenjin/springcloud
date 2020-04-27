package com.xwj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.entity.OrderInfo;
import com.xwj.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

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

	public OrderInfo save(OrderInfo user) {
		return repository.save(user);
	}

}
