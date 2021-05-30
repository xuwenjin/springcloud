package com.xwj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.xwj.entity.XwjUser;
import com.xwj.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<XwjUser> findAll() {
		return userRepository.findAll();
	}

	public XwjUser save(XwjUser user) {
		return userRepository.save(user);
	}

	/**
	 * 通过主键id查询
	 */
	public XwjUser findById(Long id) {
		Optional<XwjUser> optional = userRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	/**
	 * 通过名称查询
	 */
	public List<XwjUser> findByName(String name) {
		String sql = "select * from xwj_user where name like ?";

		Object[] paramArr = new Object[1];
		paramArr[0] = name + "%";

		return jdbcTemplate.query(sql, paramArr, new BeanPropertyRowMapper<>(XwjUser.class));
	}

}
