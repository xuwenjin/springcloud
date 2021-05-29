package com.xwj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xwj.entity.XwjUser;

public interface UserRepository extends JpaRepository<XwjUser, Long>{

}
