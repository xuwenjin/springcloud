package com.xwj.testtransaction;

import javax.persistence.Entity;

import com.xwj.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User2 extends BaseEntity {

	private String name;

}