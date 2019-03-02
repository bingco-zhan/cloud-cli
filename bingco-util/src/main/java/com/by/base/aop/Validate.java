package com.by.base.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {

    String table();

    String[] wheres();

    /**
     * 校验结果值(用于某种情况反转校验结果)
     */
    boolean resultBoolean() default false;

    String message();
}
