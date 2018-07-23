package com.xwj.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.xwj.entity.UserEntity;

/**
 * 使用feign测试hystrix
 * 
 * @author xwj
 */
@FeignClient(name = "service-provider", fallback = UserFallback.class)
public interface UserFeignClient {

	@GetMapping("/find/{id}")
	UserEntity findById(@PathVariable("id") Long id); // PathVariable必须得设置value

}
