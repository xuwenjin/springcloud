package com.xwj.dbdef;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.StringUtils;

/**
 * 数据库相关工具类
 */
public class DbUtil {

	public static final String BLANK = " "; // 空格

	// 基本类型
	public static final String[] BASIC_TYPE = { "type", "short", "int", "long", "float", "double", "boolean", "char" };

	/**
	 * 根据jpa默认生成数据库字段类型转换
	 * 
	 * @param fieldType
	 *            实体类字段类型
	 * @param isPriKey
	 *            是否主键
	 * @return
	 */
	public static String getColumnType(String fieldType, String length, boolean isPriKey) {
		if (isPriKey) {
			return " BIGINT(20) NOT NULL ";
		}
		String columnDef = JdbcTypeEnum.getColumnDef(fieldType, length);
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

	public static void main(String[] args) {
		System.out.println(isBasicType(null));
	}

}
