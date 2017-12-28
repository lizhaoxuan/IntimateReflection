package me.ele.intimate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface RefTarget {
    int DEFAULT = 0;
    int NEEDED_THROW = 1;
    int UN_NEEDED_THROW = 2;

    String className();

    boolean needForName() default false;

    boolean needReflection();

    int needThrow() default DEFAULT;
}
