package com.bonc.service.sql.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ProgramTransaction {
	String txManagerName();
	//后期考虑添加TransactionDefinition信息
	String info() default "Nana";
}
