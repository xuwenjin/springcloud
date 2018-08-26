package com.xwj.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * security-浏览器相关配置
 */
@Getter
@Setter
public class BrowserProperty {

	private String loginPage = "/login.html"; // 登录页
	
	private LoginType loginType = LoginType.JSON; //登录类型

}
