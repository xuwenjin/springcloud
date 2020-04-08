package com.xwj.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息实体
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyMessage {

	/** 用户id */
	private String userId;

	/** 消息内容 */
	private String message;
}
