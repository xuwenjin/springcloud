package com.xwj.testtransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试事物传播性
 * 
 * 如果是在同一个类中的方法调用，则不会被方法拦截器拦截到，因此事务不会起作用，必须将方法放入另一个类，并且该类通过spring注入
 * 
 * @author xwj
 */
@RestController
@RequestMapping("transaction")
public class TestTransactionController {

	@Autowired
	private TransactionRequiredService requiredService;

	@Autowired
	private TransactionRequiredNewService requiredNewService;

	/**
	 * 外围方法未开启事物-内部事物PROPAGATION_REQUIRED
	 * 
	 * 在外围方法未开启事务的情况下，Propagation.REQUIRED修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰。
	 * 
	 * 在外围方法未开启事务的情况下，如果内部方法也未开启事物，内外方法互不干扰
	 */
	@GetMapping("test1")
	public void test1() {
//		requiredService.notransaction_exception_required_required();
//		requiredService.notransaction_required_required_exception();
		requiredService.notransaction_exception_notransaction();
	}

	/**
	 * 外围方法开启事物-内部事物PROPAGATION_REQUIRED
	 * 
	 * 在外围方法开启事务的情况下，Propagation.REQUIRED修饰的内部方法会加入到外围方法的事务中，所有Propagation.REQUIRED修饰
	 * 的内部方法和外围方法均属于同一事务，只要一个方法回滚，整个事务均回滚
	 * 
	 * 在外围方法开启事务的情况下，如果内部方法也未开启事物，则内部方法相当于写在外围方法中一样，即内外所有方法是同一事物
	 */
	@GetMapping("test2")
	public void test2() {
//		requiredService.transaction_exception_required_required();
//		requiredService.transaction_required_required_exception();
//		requiredService.transaction_required_required_exception_try();
//		requiredService.transaction_exception_notransaction();
		requiredService.transaction_notransaction_exception();
	}

	/**
	 * 外围方法未开启事物-内部事物PROPAGATION_REQUIRES_NEW
	 * 
	 * 在外围方法未开启事务的情况下，Propagation.REQUIRES_NEW修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰。
	 */
	@GetMapping("test3")
	public void test3() {
//		requiredNewService.notransaction_exception_requiresNew_requiresNew();
		requiredNewService.notransaction_requiresNew_requiresNew_exception();
	}

	/**
	 * 外围方法开启事物-内部事物PROPAGATION_REQUIRES_NEW
	 * 
	 * 在外围方法开启事务的情况下，Propagation.REQUIRES_NEW修饰的内部方法依然会单独开启独立事务，且与外部方法事务也独立，内部方法
	 * 之间、内部方法和外部方法事务均相互独立，互不干扰。
	 */
	@GetMapping("test4")
	public void test4() {
//		requiredNewService.transaction_exception_required_requiresNew_requiresNew();
//		requiredNewService.transaction_required_requiresNew_requiresNew_exception();
		requiredNewService.transaction_required_requiresNew_requiresNew_exception_try();
	}

	/**
	 * 外围方法开启事物
	 * 
	 * 当@Transactional作用于非public方法时，@Transactional不生效
	 * 
	 */
	@GetMapping("test5")
	public void test5() {
		requiredNewService.transaction_required_requiresNew_exception_protected();
	}

}
