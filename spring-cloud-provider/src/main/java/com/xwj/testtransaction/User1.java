package com.xwj.testtransaction;

import javax.persistence.Entity;

import com.xwj.core.domain.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User1 extends BaseEntity {

	private String name;

}