package com.xwj.dbdef;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 生成表备注服务
 */
@Component
public class CommentService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static Map<String, String> tableMap = new ConcurrentHashMap<>();

	/**
	 * 创建表备注
	 */
	public void createComment() {
		ApplicationContext context = ContextUtils.getApplicationContext();
		if (context != null) {
			Map<String, Object> beanMap = context.getBeansWithAnnotation(TableDef.class);
			System.out.println("----------" + beanMap);
			for (String key : beanMap.keySet()) {
				Object bean = beanMap.get(key);
				if (bean != null) {
					Object target = null;
					try {
						// 如果是代理对象，需要转换为目标对象
						target = AopTargetUtils.getTarget(bean);
					} catch (Exception e) {
						e.printStackTrace();
					}
					createTableComment(target.getClass());
					createColumnCommnet(target.getClass());
				}
			}
		}
	}

	/**
	 * 创建表备注
	 * 
	 * @param clazz
	 */
	private void createTableComment(Class<?> clazz) {
		if (clazz == null) {
			return;
		}
		TableDef annotation = clazz.getAnnotation(TableDef.class);
		String tableCommnet = annotation.value(); // 表备注
		String tableName = TableBeanUtil.propertyToField(clazz.getSimpleName()); // 表名
		tableMap.put(clazz.getSimpleName(), tableName);
		doUpdateTable(tableName, tableCommnet);
	}

	/**
	 * 创建表字段备注
	 * 
	 * @param clazz
	 */
	private void createColumnCommnet(Class<?> cls) {
		String tableName = tableMap.get(cls.getSimpleName());
		Field[] fs = cls.getDeclaredFields();
		List<ColumnBean> columnList = new ArrayList<>();
		for (Field filed : fs) {
			Id id = filed.getAnnotation(Id.class);
			ColumnDef column = filed.getAnnotation(ColumnDef.class);
			if (column != null && StringUtils.isNotEmpty(column.value())) {
				boolean isPriKey = id != null; // 是否主键
				String columnComment = column.value(); // 字段备注
				String length = column.length();// 字段长度
				String columnName = TableBeanUtil.propertyToField(filed.getName()); // 数据库字段名
				String columnType = DbUtil.getColumnType(filed.getType().getSimpleName(), length, isPriKey); // 数据库字段类型
				columnList.add(new ColumnBean(columnName, columnType, columnComment));
			}
		}
		doUpdateColumn(tableName, columnList);
	}

	private void doUpdateTable(String tableName, String tableComment) {
		String sql = "ALTER TABLE " + tableName + " COMMENT '" + tableComment + "'";
		jdbcTemplate.update(sql);
	}

	/**
	 * 批量更新数据库列名备注
	 */
	public void doUpdateColumn(String tableName, List<ColumnBean> columnList) {
		String sql = DbUtil.getDbAlterSql(tableName, columnList);
		jdbcTemplate.update(sql);
	}

}
