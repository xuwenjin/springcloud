package com.xwj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.xwj.spring.XwjImportSelector;

@Import(XwjImportSelector.class)
@Configuration
public class ServiceConfig {

}
