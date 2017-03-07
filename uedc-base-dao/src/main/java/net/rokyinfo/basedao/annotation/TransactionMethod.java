/**
 * 项目名称：roky-firm
 * 文件名：TransactionAnnotation.java
 * 
 * Copyright 无锡锐祺  2016
 * 版权所有
 * 
 */
package net.rokyinfo.basedao.annotation;

import java.lang.annotation.*;

/**
 * 
 * 类描述:使用了此注解的方法需要用到事务
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TransactionMethod {

    String value() default "";
}
