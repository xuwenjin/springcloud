package com.xwj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xwj.entity.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, Long> {

}
