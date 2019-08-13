package com.xwj.testtransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class User1Service {

	@Autowired
	private User1Repository user1Repository;

	@Transactional(propagation = Propagation.REQUIRED)
	public void addRequired(User1 user) {
		user1Repository.save(user);
	}

	public void addRequiredNoTransaction(User1 user) {
		user1Repository.save(user);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addRequiresNew(User1 user) {
		user1Repository.save(user);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	protected void addRequiresNewProtected(User1 user) {
		user1Repository.save(user);
	}

}
