package com.xwj.datasource.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class User2Service {

	@Autowired
	@Qualifier("secondJdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	
	public void addDoc(){
		String sql = "select 1 from doc where ent_id = 30";
		Map<String, Object> resMap = jdbcTemplate.queryForMap(sql);
		System.out.println(resMap);
		
//		jdbcTemplate.update("insert into doc(ent_id, dr, seq_num) values(?, ?, ?)", 30, 0, 1);
	}

}
