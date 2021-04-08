package com.xwj.entity;

import java.io.Serializable;

import javax.persistence.Entity;

import com.xwj.annotations.ColumnDef;
import com.xwj.annotations.TableDef;
import com.xwj.core.domain.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@TableDef("用户信息")
public class UserInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2169427939264532306L;

	@ColumnDef("用户名")
	public String username; // 账号

	@ColumnDef("密码")
	public String password; // 密码

	@ColumnDef("姓名")
	public String name; // 用户名称

	@ColumnDef("邮箱")
	private String email;

	private Integer age;

	public UserInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

}
