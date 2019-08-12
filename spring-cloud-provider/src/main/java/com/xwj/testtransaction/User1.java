package com.xwj.testtransaction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User1 {
	
	@Id
	@TableGenerator(name = "global_id_gen", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "global_id_gen")
	private Long id;
	
	private String name;
	
}