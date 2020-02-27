package com.xwj.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.xwj.enums.LimitType;

/**
 * 请求限流
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestLimit {

	/**
	 * 资源的key
	 */
	String key() default "";

	/**
	 * Key的prefix
	 */
	String prefix() default "";

	/**
	 * 给定的时间段(秒)
	 */
	int period();

	/**
	 * 最多的访问限制次数
	 */
	int count();

	/**
	 * 限流类型
	 */
	LimitType limitType() default LimitType.CUSTOMER;

}