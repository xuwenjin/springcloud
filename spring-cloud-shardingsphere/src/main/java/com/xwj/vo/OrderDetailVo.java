package com.xwj.vo;

import lombok.Data;

@Data
public class OrderDetailVo {

	private Long orderId;

	/** 订单状态 */
	private String status;

	/** 订单类型 */
	private int orderType;

	/** 订单描述 */
	private String description;

}
