package com.xwj.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 订单表(业务表)
 */
@Entity
@Data
@Table(name = "t_order")
public class OrderInfo {

	@Id
	private Long id;

	/** 订单状态 */
	private String status;

	/** 订单类型 */
	private int orderType;

}
