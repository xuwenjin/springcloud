package com.xwj.dbdef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ColumnBean {

	private String name; // 列名

	private String type; // 列类型

	private String comment; // 类备注

}
