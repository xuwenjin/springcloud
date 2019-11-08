package com.xwj.service;

import java.util.List;

import com.xwj.entity.MyUser;

public interface IUserService {

	List<MyUser> findAll();

	MyUser findById(Long id);

	MyUser save(MyUser user);

}
