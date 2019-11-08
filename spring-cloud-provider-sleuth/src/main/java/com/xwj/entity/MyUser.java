package com.xwj.entity;

import javax.persistence.Entity;

import com.xwj.core.domain.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MyUser extends BaseEntity {

	private String username;

	private Integer age;

}
