package com.xwj.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 字典表(公共表)
 */
@Entity
@Data
@Table(name = "t_dict")
public class SysDict {

	@Id
	private Long id;

	private String code;

	private String value;

}
