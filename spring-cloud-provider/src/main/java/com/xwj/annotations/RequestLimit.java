package com.xwj.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求限流
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestLimit {

	/**
	 * 给定的时间段(秒)
	 */
	long period() default 60;

	/**
	 * 最多的访问限制次数
	 */
	long count() default Integer.MAX_VALUE;

}