package com.xwj.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.xwj.dbdef.ColumnDef;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

	@Id
	@TableGenerator(name = "global_id_gen", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "global_id_gen")
	private Long id;

	@ColumnDef("用户名")
	private String lastName;

	@ColumnDef("邮箱")
	private String email;

	private Integer age;

}
