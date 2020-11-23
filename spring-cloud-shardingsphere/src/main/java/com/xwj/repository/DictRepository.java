package com.xwj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xwj.entity.SysDict;

public interface DictRepository extends JpaRepository<SysDict, Long> {
	

}
