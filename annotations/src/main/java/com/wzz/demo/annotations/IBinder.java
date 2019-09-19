package com.wzz.demo.annotations;

/**
 * 用于实现绑定的抽象接口
 *
 * @author wangzhenzhou
 * @createTime 2019-09-06 17:37
 */
public interface IBinder<T> {
    void bind(T target);
}
