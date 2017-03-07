package net.rokyinfo.appcontroller.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2016/4/20.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MappingMethod {

    String value() default "doExecute";
}
