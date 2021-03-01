package com.xwj.spring;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class XwjImportSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		// 要配置全路径类名
		return new String[] { "com.xwj.service.spring.MyService3" };
	}

}
