package com.wzz.demo.ioc_apt_javapoet_demo;

import android.app.Activity;

import com.wzz.demo.annotations.IBinder;

/**
 * 绑定工具类
 *
 * @author wangzhenzhou
 * @createTime 2019-09-06 18:51
 */
public class MyButterKnife {
    public static void bind(Activity activity) {
        String name = activity.getClass().getName() + "_ViewBinding";
        try {
            Class<?> bindClazz = Class.forName(name);
            IBinder  iBinder   = (IBinder) bindClazz.newInstance();
            //利用面向对象的多态执行中间类 activityName_ViewBinding的bind方法
            iBinder.bind(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
