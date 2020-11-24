package com.xwj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xwj.entity.OrderInfoDetail;

public interface OrderDetailRepository extends JpaRepository<OrderInfoDetail, Long> {
	

}
