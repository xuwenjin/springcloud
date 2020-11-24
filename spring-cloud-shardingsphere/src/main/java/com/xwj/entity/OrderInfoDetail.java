package com.xwj.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 订单表字表(业务表)
 */
@Entity
@Data
@Table(name = "t_order_detail")
public class OrderInfoDetail {

	@Id
	private Long id;

	/** 订单id */
	private Long orderId;

	/** 订单类型 */
	private int orderType;

	/** 订单描述 */
	private String description;

}
