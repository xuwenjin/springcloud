package com.xwj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.xwj.vo.OrderDetailVo;

@Component
public class OrderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate template; // 具名参数

	@PostConstruct
	public void init() {
		template = new NamedParameterJdbcTemplate(jdbcTemplate);
	}

	/**
	 * 连表查询订单和订单详情(分页查询)
	 */
	public List<OrderDetailVo> queryOrderDetailList(int start, int offset) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select o.id orderId, o.order_type orderType, o.status, d.description  ");
		sql.append(" from t_order o join t_order_detail d on o.id = d.order_id ");
		sql.append(" order by o.id desc ");
		sql.append(" limit :start, :offset ");

		Map<String, Object> paramMap = new HashMap<>(2);
		paramMap.put("start", start);
		paramMap.put("offset", offset);
		return template.query(sql.toString(), paramMap, new BeanPropertyRowMapper<>(OrderDetailVo.class));
	}

	public List<Map<String, Object>> queryGroupList() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select order_type, count(1) num ");
		sql.append(" from t_order ");
		sql.append(" group by order_type ");
		sql.append(" order by order_type ");
		return jdbcTemplate.queryForList(sql.toString());
	}

}
