package com.xwj.operlog;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OperLogService {

	@Autowired
	private OperLogRepository repository;

	@Transactional
	public void saveLog(OperLog operLog) {
		repository.save(operLog);
	}

}
