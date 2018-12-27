package com.xuanxuan.csu.configurer.announce;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登陆注解拦截器,在方法上加入此注解则会被拦截
 * *
 * * 在需要登录验证的Controller的方法上使用此注解
 */

@Target({ElementType.METHOD})// 可用在方法名上
@Retention(RetentionPolicy.RUNTIME)// 运行时有效
public @interface LoginRequired {

}