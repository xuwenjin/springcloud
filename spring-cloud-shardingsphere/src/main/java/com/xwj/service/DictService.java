package com.xwj.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwj.entity.SysDict;
import com.xwj.repository.DictRepository;

@Service
public class DictService {

	@Autowired
	private DictRepository repository;

	public SysDict findById(Long id) {
		Optional<SysDict> optional = repository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public List<SysDict> findAll() {
		return repository.findAll();
	}

	public SysDict save(SysDict dict) {
		return repository.save(dict);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

}
