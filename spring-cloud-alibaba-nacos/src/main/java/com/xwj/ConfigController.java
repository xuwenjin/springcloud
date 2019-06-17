package com.xwj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试nacos作为配置中心
 * 
 * @author xwj
 */
@RestController
@RefreshScope // 开启配置文件修改的监听，并刷新。只能配置在类上，配置在方法上不生效
@RequestMapping("config")
public class ConfigController {

	@Value("${xwj.name:}")
	private String name;

	@GetMapping("/test")
	public String test() {
		return name;
	}

}
