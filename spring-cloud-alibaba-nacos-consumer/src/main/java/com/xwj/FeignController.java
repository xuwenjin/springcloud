package com.xwj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {

	@Autowired
	private Client feignClient;

	@GetMapping("/test")
	public String test() {
		String result = feignClient.hello("小红帽");
		return "Return : " + result;
	}

}
