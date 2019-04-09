package com.xwj.dbdef;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JdbcTypeEnum {

	STRING("VARCHAR", "255"), //
	INTEGER("INT", "11"), //
	INT("INT", "11"), //
	BIGDECIMAL("DECIMAL", "19,2"), //
	LONG("BIGINT", "20"), //
	DOUBLE("DOUBLE", ""), //
	BOOLEAN("BIT", "1"), //
	DATE("DATETIME", "6");

	private String dbType; // 数据库字段类型

	private String dbLength; // 数据库字段长度

	/**
	 * 获取数据库字段定义
	 * 
	 * @param fieldType
	 *            实体字段类型
	 * @param length
	 *            数据库字段长度。如(19,2)、255
	 */
	public static final String getColumnDef(String fieldType, String dbLength) {
		if (StringUtils.isEmpty(fieldType) || !isContain(fieldType)) {
			return " VARCHAR(255) ";
		}
		String lengthStr = null;
		JdbcTypeEnum typeEnum = JdbcTypeEnum.valueOf(fieldType.toUpperCase());
		if (StringUtils.isNotEmpty(dbLength)) {
			lengthStr = "(" + dbLength + ")";
		} else {
			if (StringUtils.isNotEmpty(typeEnum.getDbLength())) {
				lengthStr = "(" + typeEnum.getDbLength() + ") ";
			}
		}
		return typeEnum.getDbType() + lengthStr;
	}

	/**
	 * 是否属于定义的类型
	 */
	public static boolean isContain(String fieldType) {
		return Arrays.stream(JdbcTypeEnum.values()).filter(d -> d.name().equals(fieldType.toUpperCase())).findAny()
				.isPresent();
	}

}
