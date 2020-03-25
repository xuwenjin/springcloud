package com.xwj.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.xwj.annotations.ColumnDef;
import com.xwj.annotations.TableDef;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@TableDef("用户信息")
public class UserInfo {

	@Id
	@TableGenerator(name = "global_id_gen", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "global_id_gen")
	private Long id;

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
