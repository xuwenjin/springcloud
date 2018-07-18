package com.xwj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xwj.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
