package com.xwj.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class SecurityResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 秘钥 */
	private String key;

	/** 数据 */
	private String data;

}