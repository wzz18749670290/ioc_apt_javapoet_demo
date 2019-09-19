package com.wzz.demo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定ContentView注解
 * @author wangzhenzhou
 * @createTime 2019-09-06 17:37
 */
@Retention(RetentionPolicy.SOURCE)  //生命注解的生命周期 java->class->runtime
@Target(ElementType.TYPE)           //该注解作用在类上
public @interface BindContentView {
    int value();
}
