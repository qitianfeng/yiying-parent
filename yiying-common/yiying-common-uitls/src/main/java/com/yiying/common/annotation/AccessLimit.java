package com.yiying.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author zzf
 * @version 1.0
 * @date 2020/10/18 20:56
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {

    //设置redis的过期时间
    int seconds();
    //访问的最大次数
    int maxCount();
    //是否登录
    boolean needLogin()default true;
}