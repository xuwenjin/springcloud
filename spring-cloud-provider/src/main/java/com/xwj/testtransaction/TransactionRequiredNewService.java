package com.xwj.testtransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * PROPAGATION_REQUIRES_NEW事物传播测试
 * 
 * @author xwj
 */
@Service
public class TransactionRequiredNewService {

	@Autowired
	private User1Service user1Service;

	@Autowired
	private User2Service user2Service;

	/************ 外围方法未开启事物 *************/

	public void notransaction_exception_requiresNew_requiresNew() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequiresNew(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiresNew(user2);
		throw new RuntimeException();
	}

	public void notransaction_requiresNew_requiresNew_exception() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequiresNew(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiresNewException(user2);
	}

	/************ 外围方法开启事物 *************/

	@Transactional(propagation = Propagation.REQUIRED)
	public void transaction_exception_required_requiresNew_requiresNew() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequired(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiresNew(user2);
		throw new RuntimeException();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void transaction_required_requiresNew_requiresNew_exception() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequired(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiresNew(user2);

		User2 user3 = new User2();
		user3.setName("王五");
		user2Service.addRequiresNewException(user3);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void transaction_required_requiresNew_requiresNew_exception_try() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequired(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiresNew(user2);

		User2 user3 = new User2();
		user3.setName("王五");
		try {
			user2Service.addRequiresNewException(user3);
		} catch (Exception e) {
			System.out.println("回滚");
		}
	}

	/**
	 * 外围方法开启事物-测试将@Transactional作用于私有方法
	 * 
	 * 内部方法抛异常，外围方法捕获
	 */
	@Transactional
	public void transaction_required_requiresNew_exception_protected() {
		User1 user1 = new User1();
		user1.setName("张三");
		user1Service.addRequiresNew(user1);

		User2 user2 = new User2();
		user2.setName("李四");
		user2Service.addRequiresNewProtected(user2);
	}

}
