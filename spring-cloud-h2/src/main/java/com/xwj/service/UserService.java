package com.xwj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.entity.XwjUser;
import com.xwj.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public XwjUser findById(Long id) {
		Optional<XwjUser> optional = userRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public List<XwjUser> findAll() {
		return userRepository.findAll();
	}

	public XwjUser save(XwjUser user) {
		return userRepository.save(user);
	}

}
