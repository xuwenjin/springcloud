package com.xwj.entity;

import javax.persistence.Entity;

import com.xwj.core.domain.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserInfo extends BaseEntity {

	private String name; // 账号

	private String phone;

	private int num;

	private int age;

}
