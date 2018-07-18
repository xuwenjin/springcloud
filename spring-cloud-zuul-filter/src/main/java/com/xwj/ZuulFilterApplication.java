package com.xwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringBootApplication
public class ZuulFilterApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ZuulFilterApplication.class, args);
	}
	
	@Bean
	public PreZuulFilter preZuulFilter(){
		return new PreZuulFilter();
	}

}
