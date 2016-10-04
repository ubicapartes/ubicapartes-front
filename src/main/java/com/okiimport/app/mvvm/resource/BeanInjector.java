package com.okiimport.app.mvvm.resource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface BeanInjector {
	String value(); //beanName
	Class<?> clase() default Class.class; //class of Bean
}
