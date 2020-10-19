package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xwj.entity.UserInfoVo;

@RestController
public class HystrixController {

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 测试hystrix
	 * 
	 * 1、调用远程服务超时后，断路器打开，调用getOneFallBack (如果远程服务挂了，会立马调用getOneFallBack，不会使用超时)
	 * 2、超时时间为1000毫秒(默认1秒)
	 */
	@GetMapping("/getOne/{id}")
	@HystrixCommand(fallbackMethod = "getOneFallBack", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000") })
	public UserInfoVo getOne(@PathVariable Long id) {
		UserInfoVo user = restTemplate.getForObject("http://service-provider/find/" + id, UserInfoVo.class);
		return user;
	}

	/**
	 * 参数跟返回类型必须跟上面的一样，不然会报找不到该方法的错
	 */
	public UserInfoVo getOneFallBack(Long id) {
		UserInfoVo user = new UserInfoVo();
		user.setId("1000");
		user.setAge(12);
		return user;
	}

}
