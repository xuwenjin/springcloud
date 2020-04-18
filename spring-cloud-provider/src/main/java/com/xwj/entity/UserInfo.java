package com.xwj.entity;

import javax.persistence.Entity;

import com.xwj.annotations.ColumnDef;
import com.xwj.annotations.TableDef;
import com.xwj.core.domain.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@TableDef("用户信息")
public class UserInfo extends BaseEntity {

	@ColumnDef("用户名")
	public String username; // 账号

	@ColumnDef("密码")
	public String password; // 密码

	@ColumnDef("姓名")
	public String name; // 用户名称

	@ColumnDef("邮箱")
	private String email;

	private Integer age;

}
