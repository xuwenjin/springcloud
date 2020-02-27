package com.xwj.common;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class AppUserLogVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mobile;
	private String ip;
	private String token;
	private String url;
	private String actionType;
	private Date actionTime;
	private String actionResult;
	private Date createTime;

}