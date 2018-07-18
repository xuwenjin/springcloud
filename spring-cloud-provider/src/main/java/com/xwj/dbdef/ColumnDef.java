package com.xwj.dbdef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnDef {

	/**
	 * 备注
	 */
	String value() default "";

	/**
	 * 长度。如 255、19,2
	 */
	String length() default "";

}
