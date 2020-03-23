package com.xwj.service;

import java.util.List;

import com.xwj.entity.UserInfo;

public interface IUserService {

	List<UserInfo> findAll();

	UserInfo findById(Long id);

	UserInfo validate(String username, String password);

	UserInfo save(UserInfo user);

	void saveAll(List<UserInfo> userList);

}
