package com.xwj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xwj.entity.MyUser;

public interface UserRepository extends JpaRepository<MyUser, Long>{

}
