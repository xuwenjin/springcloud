package com.xwj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xwj.entity.OrderInfo;

public interface UserRepository extends JpaRepository<OrderInfo, Long> {

}
