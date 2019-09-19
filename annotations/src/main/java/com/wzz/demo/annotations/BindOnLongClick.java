package com.wzz.demo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定长按事件注解
 * @author wangzhenzhou
 * @createTime 2019-09-06 17:37
 */
@Retention(RetentionPolicy.SOURCE)  //生命注解的生命周期 java->class->runtime
@Target(ElementType.METHOD)         //该注解作用在方法上
public @interface BindOnLongClick {
    int[] value();
}
