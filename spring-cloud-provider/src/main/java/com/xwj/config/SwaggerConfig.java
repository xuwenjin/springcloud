package com.xwj.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 // 开始Swagger2
public class SwaggerConfig {

	/**
	 * 配置docket实例(如果不配置，就用默认的)
	 */
	@SuppressWarnings("rawtypes")
	@Bean
	public Docket docket() {
		ApiInfo apiInfo = new ApiInfo("老许子的接口文档", "Api Documentation", "1.0", "urn:tos", ApiInfo.DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
				new ArrayList<VendorExtension>());

		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo)
				// enable：是否启动swagger。如果为false，则swagger不能在浏览器访问
				// .enable(false)
				.groupName("老许子") // 分组。默认default
				// 选择器
				.select()
				// RequestHandlerSelectors：配置要扫描接口的方式
				// basePackage：扫描的包
				// any: 全部扫描
				// withClassAnnotation：扫描类上的注解
				// withMethodAnnotation：扫描方法上的注解
				.apis(RequestHandlerSelectors.basePackage("com.xwj")).build();
	}

}
