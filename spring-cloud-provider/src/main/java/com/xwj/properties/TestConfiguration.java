package com.xwj.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 测试@ConditionalOnProperty
 */
@Data
@Configuration
@ConditionalOnProperty(prefix = "test", name = "conditional", havingValue = "abc", matchIfMissing = true)
public class TestConfiguration {

	/** 默认值 */
	private String field = "default";

}
