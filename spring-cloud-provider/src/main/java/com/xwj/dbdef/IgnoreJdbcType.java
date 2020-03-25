package com.xwj.dbdef;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 需要忽略的类型
 */
@Getter
@AllArgsConstructor
public enum IgnoreJdbcType {

	Id,

	Lob,

	ManyToOne,

	OneToOne,

	OneToMany,

	ManyToMany;

	/**
	 * 是否属于定义的类型
	 */
	public static boolean isContain(String fieldType) {
		return Arrays.stream(IgnoreJdbcType.values()).filter(d -> d.name().equals(fieldType)).findAny().isPresent();
	}

}
