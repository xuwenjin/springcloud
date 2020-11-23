package com.xwj.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "t_order")
public class OrderInfo {

	@Id
	private Long id;

	private String status;

	private int orderType;

}
