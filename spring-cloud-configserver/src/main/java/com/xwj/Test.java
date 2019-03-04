//import java.util.Arrays;
//
//import org.springframework.boot.Banner.Mode;
//import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.context.config.ConfigFileApplicationListener;
//import org.springframework.cloud.config.environment.Environment;
//import org.springframework.cloud.config.server.environment.PassthruEnvironmentRepository;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.core.env.ConfigurableEnvironment;
//
/////*
//// * Copyright 2013-2015 the original author or authors.
//// *
//// * Licensed under the Apache License, Version 2.0 (the "License");
//// * you may not use this file except in compliance with the License.
//// * You may obtain a copy of the License at
//// *
//// *      http://www.apache.org/licenses/LICENSE-2.0
//// *
//// * Unless required by applicable law or agreed to in writing, software
//// * distributed under the License is distributed on an "AS IS" BASIS,
//// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//// * See the License for the specific language governing permissions and
//// * limitations under the License.
//// */
////package org.springframework.cloud.config.server.environment;
////
////import java.io.IOException;
////import java.util.ArrayList;
////import java.util.Collection;
////import java.util.Collections;
////import java.util.Iterator;
////import java.util.LinkedHashMap;
////import java.util.List;
////import java.util.Map;
////import java.util.Map.Entry;
////import java.util.TreeMap;
////
////import javax.servlet.http.HttpServletResponse;
////
////import com.fasterxml.jackson.databind.ObjectMapper;
////
////import org.yaml.snakeyaml.DumperOptions.FlowStyle;
////import org.yaml.snakeyaml.Yaml;
////import org.yaml.snakeyaml.nodes.Tag;
////
////import org.springframework.cloud.config.environment.Environment;
////import org.springframework.cloud.config.environment.PropertySource;
////import org.springframework.cloud.config.server.environment.EnvironmentRepository;
////import org.springframework.http.HttpHeaders;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.MediaType;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.ExceptionHandler;
////import org.springframework.web.bind.annotation.PathVariable;
////import org.springframework.web.bind.annotation.RequestMapping;
////import org.springframework.web.bind.annotation.RequestMethod;
////import org.springframework.web.bind.annotation.RequestParam;
////import org.springframework.web.bind.annotation.RestController;
////
////import static org.springframework.cloud.config.server.support.EnvironmentPropertySource.prepareEnvironment;
////import static org.springframework.cloud.config.server.support.EnvironmentPropertySource.resolvePlaceholders;
////
/////**
//// * @author Dave Syer
//// * @author Spencer Gibb
//// * @author Roy Clarkson
//// * @author Bartosz Wojtkiewicz
//// * @author Rafal Zukowski
//// * @author Ivan Corrales Solera
//// * @author Daniel Frey
//// * @author Ian Bondoc
//// *
//// */
////@RestController
////@RequestMapping(method = RequestMethod.GET, path = "${spring.cloud.config.server.prefix:}")
////public class EnvironmentController {
////	
////	private EnvironmentRepository repository;
////	
////	// ...
////	
////	@RequestMapping({ "/{label}/{name}-{profiles}.yml", "/{label}/{name}-{profiles}.yaml" })
////	public ResponseEntity<String> labelledYaml(@PathVariable String name, @PathVariable String profiles,
////			@PathVariable String label, @RequestParam(defaultValue = "true") boolean resolvePlaceholders)
////			throws Exception {
////		// 校验profiles是否含义"-"，如果有则抛出异常
////		validateProfiles(profiles);
////		//获取环境信息，即远程配置文件数据
////		Environment environment = labelled(name, profiles, label);
////		//将environment对象转为Map
////		Map<String, Object> result = convertToMap(environment);
////		if (this.stripDocument && result.size() == 1 && result.keySet().iterator().next().equals("document")) {
////			Object value = result.get("document");
////			if (value instanceof Collection) {
////				return getSuccess(new Yaml().dumpAs(value, Tag.SEQ, FlowStyle.BLOCK));
////			} else {
////				return getSuccess(new Yaml().dumpAs(value, Tag.STR, FlowStyle.BLOCK));
////			}
////		}
////		String yaml = new Yaml().dumpAsMap(result);
////
////		if (resolvePlaceholders) {
////			yaml = resolvePlaceholders(prepareEnvironment(environment), yaml);
////		}
////
////		return getSuccess(yaml);
////	}
////	
////	// ...
////	
////	
////@RequestMapping("/{name}/{profiles}/{label:.*}")
////public Environment labelled(@PathVariable String name, @PathVariable String profiles,
////		@PathVariable String label) {
////	if (name != null && name.contains("(_)")) {
////		// "(_)" is uncommon in a git repo name, but "/" cannot be matched
////		// by Spring MVC
////		name = name.replace("(_)", "/");
////	}
////	if (label != null && label.contains("(_)")) {
////		// "(_)" is uncommon in a git branch name, but "/" cannot be matched
////		// by Spring MVC
////		label = label.replace("(_)", "/");
////	}
////	Environment environment = this.repository.findOne(name, profiles, label);
////	return environment;
////}
////
////}
//
//
//@Override
//public Environment findOne(String config, String profile, String label) {
//	SpringApplicationBuilder builder = new SpringApplicationBuilder(
//			PropertyPlaceholderAutoConfiguration.class);
//	ConfigurableEnvironment environment = getEnvironment(profile);
//	builder.environment(environment);
//	builder.web(false).bannerMode(Mode.OFF);
//	if (!logger.isDebugEnabled()) {
//		// Make the mini-application startup less verbose
//		builder.logStartupInfo(false);
//	}
//	String[] args = getArgs(config, profile, label);
//	// Explicitly set the listeners (to exclude logging listener which would change
//	// log levels in the caller)
//	builder.application()
//			.setListeners(Arrays.asList(new ConfigFileApplicationListener()));
//	ConfigurableApplicationContext context = builder.run(args);
//	environment.getPropertySources().remove("profiles");
//	try {
//		return clean(new PassthruEnvironmentRepository(environment).findOne(config,
//				profile, label));
//	}
//	finally {
//		context.close();
//	}
//}
