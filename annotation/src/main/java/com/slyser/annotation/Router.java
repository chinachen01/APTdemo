package com.slyser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: chenyong(<a href="chenyong@danlu.com">chenyong@danlu.com</a>)<br/>
 * version: 1.0.0<br/>
 * since: 2018/7/24 下午7:43<br/>
 *
 * <p>
 * 内容描述区域
 * </p>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Router {
    String name();
}
