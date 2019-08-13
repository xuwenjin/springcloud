package com.xwj.testtransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class User2Service {

	@Autowired
	private User2Repository user2Repository;

	@Transactional(propagation = Propagation.REQUIRED)
	public void addRequired(User2 user) {
		user2Repository.save(user);
	}

	public void addRequiredNoTransaction(User2 user) {
		user2Repository.save(user);
	}

	public void addRequiredNoTransactionException(User2 user) {
		user2Repository.save(user);
		throw new RuntimeException();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void addRequiredException(User2 user) {
		user2Repository.save(user);
		throw new RuntimeException();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addRequiresNew(User2 user) {
		user2Repository.save(user);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addRequiresNewException(User2 user) {
		user2Repository.save(user);
		throw new RuntimeException();
	}

}
