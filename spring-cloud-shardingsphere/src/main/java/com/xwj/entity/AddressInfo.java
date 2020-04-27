package com.xwj.entity;

import javax.persistence.Entity;

import com.xwj.core.domain.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AddressInfo extends BaseEntity {

	private String name;
	
	private int num;

	private int code;

}
