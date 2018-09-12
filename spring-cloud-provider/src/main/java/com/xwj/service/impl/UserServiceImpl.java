package com.xwj.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.xwj.entity.User;
import com.xwj.redis.JsonRedisTemplate;
import com.xwj.repository.UserRepository;
import com.xwj.service.IUserService;

@Service
public class UserServiceImpl implements IUserService, InitializingBean {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JsonRedisTemplate redisTemplate;
	
	private Map<Long, Long> idMap = new ConcurrentHashMap<Long, Long>();
	
	private static BloomFilter<Long> bf = BloomFilter.create(Funnels.longFunnel(), 1000000);

	@Override
	public User findById(Long id) {
//		if (idMap.get(id) == null) {
//			return null;
//		}
		if (!bf.mightContain(id)) {
			return null;
		}
		
		// 先从缓存中查询，查不到读数据库
		User user = (User) redisTemplate.opsForValue().get("" + id);
		if (user == null) {
			user = userRepository.findOne(id);
		}
		return user;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<User> userList = userRepository.findAll();
		for (User user : userList) {
			idMap.put(user.getId(), user.getId());
			bf.put(user.getId());
		}
		System.out.println(idMap);
	}

}
