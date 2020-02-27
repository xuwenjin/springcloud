package com.xwj.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class SecurityResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	private String data;

}