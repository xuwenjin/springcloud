package com.xwj.testtransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * PROPAGATION_REQUIRED事物传播测试
 * 
 * @author xwj
 */
@Service
public class TransactionRequiredService {

	@Autowired
	private User1Service user1Service;

	@Autowired
	private User2Service user2Service;

	/************ 外围方法未开启事物 *************/

	/**
	 * 外围方法未开启事物，内部方法开启事物
	 * 
	 * 外围方法抛异常
	 */
	public void notransaction_exception_required_required() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequired(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequired(user2);

		throw new RuntimeException();
	}

	/**
	 * 外围方法未开启事物
	 * 
	 * 内部方法抛异常
	 */
	public void notransaction_required_required_exception() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequired(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiredException(user2);
	}

	/**
	 * 外围方法未开启事物，内部方法未开启事物
	 * 
	 * 外围方法抛异常
	 */
	public void notransaction_exception_notransaction() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequiredNoTransaction(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiredNoTransaction(user2);

		throw new RuntimeException();
	}

	/************ 外围方法开启事物 *************/

	/**
	 * 外围方法开启事物
	 * 
	 * 外围方法抛异常
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void transaction_exception_required_required() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequired(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequired(user2);

		throw new RuntimeException();
	}

	/**
	 * 外围方法开启事物，内部方法未开启事物
	 * 
	 * 外围方法抛异常
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void transaction_exception_notransaction() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequiredNoTransaction(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiredNoTransaction(user2);

		throw new RuntimeException();
	}

	/**
	 * 外围方法开启事物
	 * 
	 * 内部方法抛异常
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void transaction_required_required_exception() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequired(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiredException(user2);
	}

	/**
	 * 外围方法开启事物，内部方法未开启事物
	 * 
	 * 内部方法抛异常
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void transaction_notransaction_exception() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequiredNoTransaction(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiredNoTransactionException(user2);
	}

	/**
	 * 外围方法开启事物
	 * 
	 * 内部方法抛异常，外围方法捕获
	 */
	@Transactional
	public void transaction_required_required_exception_try() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequired(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		try {
			user2Service.addRequiredException(user2);
		} catch (Exception e) {
			System.out.println("方法回滚");
		}
	}

}
