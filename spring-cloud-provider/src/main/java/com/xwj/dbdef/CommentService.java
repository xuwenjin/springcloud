package com.xwj.dbdef;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.xwj.annotations.ColumnDef;
import com.xwj.annotations.TableDef;

import lombok.extern.slf4j.Slf4j;

/**
 * 生成表备注服务
 */
@Slf4j
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
			for (String key : beanMap.keySet()) {
				Object bean = beanMap.get(key);
				if (bean != null) {
					// 如果是代理对象，需要转换为目标对象
					Object target = AopTargetUtils.getTarget(bean);
					if (target != null) {
						this.createTableComment(target.getClass());
						this.createColumnCommnet(target.getClass());
					}
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
		this.doUpdateTable(tableName, tableCommnet);
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
			if (this.isContainIgnoreJdbcType(filed)) {
				continue;
			}
			ColumnDef column = filed.getAnnotation(ColumnDef.class);
			if (column != null && StringUtils.isNotEmpty(column.value())) {
				String columnComment = column.value(); // 字段备注
				String length = column.length();// 字段长度
				String columnName = TableBeanUtil.propertyToField(filed.getName()); // 数据库字段名
				String columnType = DbUtil.getColumnType(filed.getType().getSimpleName(), length); // 数据库字段类型
				if (StringUtils.isNotEmpty(columnName) && StringUtils.isNotEmpty(columnType)) {
					columnList.add(new ColumnBean(columnName, columnType, columnComment));
				}
			}
		}
		this.doUpdateColumn(tableName, columnList);
	}

	/**
	 * 是否包含忽略类型
	 */
	private boolean isContainIgnoreJdbcType(Field filed) {
		Annotation[] annArr = filed.getAnnotations();
		if (ArrayUtils.isNotEmpty(annArr)) {
			for (Annotation ann : annArr) {
				String annName = ann.annotationType().getSimpleName();
				if (IgnoreJdbcType.isContain(annName)) {
					log.info("包含忽略类型名称：{}", annName);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 更新表备注
	 */
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
