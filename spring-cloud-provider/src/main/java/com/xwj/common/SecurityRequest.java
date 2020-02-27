package com.xwj.common;

import java.io.Serializable;

import lombok.Data;

/**
 * 权限request
 */
@Data
public class SecurityRequest implements Serializable {

	private static final long serialVersionUID = -6282882717756071909L;

	private String key;

	private String data;

}