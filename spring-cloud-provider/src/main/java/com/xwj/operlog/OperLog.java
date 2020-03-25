package com.xwj.operlog;

import javax.persistence.Entity;
import javax.persistence.Lob;

import com.xwj.annotations.ColumnDef;
import com.xwj.core.domain.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志
 * 
 * @author xwj
 *
 */
@Entity
@Getter
@Setter
public class OperLog extends BaseEntity {

	@ColumnDef("操作员id")
	private String userId;

	@ColumnDef("操作员名称")
	private String username;

	@ColumnDef("操作说明")
	private String operation;

	@ColumnDef("操作模块")
	private String operationModule;

	@ColumnDef("操作方法")
	private String method;

	@ColumnDef("请求uri")
	private String path;

	@Lob
	@ColumnDef(value = "请求参数", length = "2000")
	private String requestParam;

	@ColumnDef(value = "响应结果", length = "2000")
	private String responseResult;

	@ColumnDef("请求ip")
	private String ip;

}
