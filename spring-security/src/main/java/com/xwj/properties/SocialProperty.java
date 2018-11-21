package com.xwj.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialProperty {

	private String filterProcessesUrl = "/auth"; // 默认请求url

	private QQProperty qq = new QQProperty();

}
