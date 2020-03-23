package com.xwj.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.entity.UserInfo;
import com.xwj.repository.UserRepository;
import com.xwj.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserInfo findById(Long id) {
		Optional<UserInfo> optional = userRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<UserInfo> findAll() {
		return userRepository.findAll();
	}

	@Override
	public UserInfo save(UserInfo user) {
		return userRepository.save(user);
	}

	@Override
	public void saveAll(List<UserInfo> userList) {
		userRepository.saveAll(userList);
	}

	@Override
	public UserInfo validate(String username, String password) {
		UserInfo userInfo = findById(1L);
		// 简单校验
		if (StringUtils.equals(username, userInfo.getUsername())
				&& StringUtils.equals(password, userInfo.getPassword())) {
			return userInfo;
		}
		return null;
	}

}
