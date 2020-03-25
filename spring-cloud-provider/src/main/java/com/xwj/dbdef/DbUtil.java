package com.xwj.dbdef;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据库相关工具类
 */
public class DbUtil {

	private static final String BLANK = " "; // 空格

	// 基本类型
	private static final String[] BASIC_TYPE = { "byte", "short", "int", "long", "float", "double", "boolean", "char" };

	/**
	 * 根据jpa默认生成数据库字段类型转换
	 * 
	 * @param fieldType
	 *            实体类字段类型
	 */
	public static String getColumnType(String fieldType, String length) {
		// 如果找不到定义的类型，则返回空
		if (StringUtils.isEmpty(fieldType) || !isContain(fieldType)) {
			return null;
		}
		String columnDef = getColumnDef(fieldType, length);
		String defaultVal = isBasicType(fieldType) ? " NOT NULL" : " NULL ";
		return columnDef + defaultVal;
	}

	/**
	 * 是否java基本类型
	 */
	public static boolean isBasicType(String fieldType) {
		return Arrays.stream(BASIC_TYPE).filter(d -> d.equals(fieldType)).findAny().isPresent();
	}

	/**
	 * 拼接插入语句
	 */
	public static String getDbAlterSql(String tableName, List<ColumnBean> columnList) {
		StringBuffer sql = new StringBuffer();
		sql.append("ALTER TABLE " + tableName).append(BLANK);
		columnList.stream().forEach(d -> {
			sql.append("CHANGE ").append(d.getName() + BLANK + d.getName() + BLANK);
			sql.append(d.getType() + BLANK);
			sql.append("COMMENT '" + d.getComment() + "',");
		});
		return StringUtils.stripEnd(sql.toString(), ",");
	}

	/**
	 * 获取数据库字段定义
	 * 
	 * @param fieldType
	 *            实体字段类型
	 * @param length
	 *            数据库字段长度。如(19,2)、255
	 */
	public static final String getColumnDef(String fieldType, String dbLength) {
		String lengthStr = "";
		JdbcTypeEnum typeEnum = JdbcTypeEnum.valueOf(fieldType.toUpperCase());
		if (StringUtils.isNotEmpty(dbLength)) {
			// 如果有定义长度，则用定义的
			lengthStr = "(" + dbLength + ")";
		} else {
			// 如果没有定义长度，则查询枚举中的定义的
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
