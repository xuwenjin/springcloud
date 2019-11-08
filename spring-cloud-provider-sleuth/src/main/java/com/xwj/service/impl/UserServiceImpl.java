package com.xwj.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.entity.MyUser;
import com.xwj.repository.UserRepository;
import com.xwj.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public MyUser findById(Long id) {
		Optional<MyUser> optional = userRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<MyUser> findAll() {
		return userRepository.findAll();
	}

	@Override
	public MyUser save(MyUser user) {
		return userRepository.save(user);
	}

}
