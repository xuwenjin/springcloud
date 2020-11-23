package com.xwj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xwj.entity.OrderInfo;

public interface OrderRepository extends JpaRepository<OrderInfo, Long> {
	

}
