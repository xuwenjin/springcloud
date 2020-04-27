package com.xwj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xwj.entity.AddressInfo;

public interface AddressRepository extends JpaRepository<AddressInfo, Long> {

}
