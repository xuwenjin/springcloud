package com.xwj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.entity.UserInfo;
import com.xwj.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public UserInfo findById(Long id) {
		Optional<UserInfo> optional = repository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public List<UserInfo> findAll() {
		return repository.findAll();
	}

	public UserInfo save(UserInfo user) {
		return repository.save(user);
	}

}
