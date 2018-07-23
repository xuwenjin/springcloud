package com.xwj.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.xwj.entity.UserEntity;

/**
 * 使用feign
 * 
 * @author xwj
 */
@FeignClient(name = "service-provider", fallback = UserFallback.class)
public interface UserFeignClient {

	/**
	 * get请求
	 */
	@GetMapping("/find/{id}")
	UserEntity findById(@PathVariable("id") Long id); // PathVariable注解必须得设置value

	/**
	 * post请求
	 */
	@PostMapping("/findUser")
	UserEntity findUser(@RequestBody UserEntity user);

}
