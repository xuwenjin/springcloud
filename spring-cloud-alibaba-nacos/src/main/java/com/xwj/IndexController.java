package com.xwj;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	@GetMapping("/hello")
	public String hello(@RequestParam String name) {
		return "hello " + name;
	}

}
