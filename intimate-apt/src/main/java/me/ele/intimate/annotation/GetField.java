package me.ele.intimate.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface GetField {

    String value();

    boolean needThrow() default false;
}
