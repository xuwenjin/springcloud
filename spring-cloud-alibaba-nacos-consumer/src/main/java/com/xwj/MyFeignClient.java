package com.xwj;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 使用feign
 */
@FeignClient(name = "nacos-client-provider")
public interface MyFeignClient {

	/**
	 * get请求
	 */
	@GetMapping("/hello")
	String hello(@RequestParam(name = "name") String name);

}
