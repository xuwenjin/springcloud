package com.xwj.dbdef;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JdbcTypeEnum {

	/**
	 * STRING
	 */
	STRING("VARCHAR", "255"),
	/**
	 * INTEGER
	 */
	INTEGER("INT", "11"),
	/**
	 * INT
	 */
	INT("INT", "11"),
	/**
	 * BIGDECIMAL
	 */
	BIGDECIMAL("DECIMAL", "19,2"),
	/**
	 * BIGINT
	 */
	LONG("BIGINT", "20"),
	/**
	 * DOUBLE
	 */
	DOUBLE("DOUBLE", ""),
	/**
	 * FLOAT
	 */
	FLOAT("FLOAT", ""),
	/**
	 * BOOLEAN
	 */
	BOOLEAN("BIT", "1"),
	/**
	 * DATE
	 */
	DATE("DATETIME", "6");

	private String dbType; // 数据库字段类型

	private String dbLength; // 数据库字段长度

}
