package com.xwj.service;

import java.util.List;

import com.xwj.entity.User;

public interface IUserService {

	List<User> findAll();

	User findById(Long id);

	User save(User user);
	
	void saveAll(List<User> userList);

}
