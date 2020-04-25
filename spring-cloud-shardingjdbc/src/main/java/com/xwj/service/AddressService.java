package com.xwj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.entity.AddressInfo;
import com.xwj.repository.AddressRepository;

@Service
public class AddressService {

	@Autowired
	private AddressRepository repository;

	public AddressInfo findById(Long id) {
		Optional<AddressInfo> optional = repository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public List<AddressInfo> findAll() {
		return repository.findAll();
	}

	public AddressInfo save(AddressInfo user) {
		return repository.save(user);
	}

}
