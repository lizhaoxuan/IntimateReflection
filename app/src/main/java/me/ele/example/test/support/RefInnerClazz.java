package me.ele.example.test.support;

import me.ele.example.mock.InnerClazz;
import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.RefTarget;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */
//@RefTarget(clazz = InnerClazz.class, optimizationRef = true)
public interface RefInnerClazz {

    @GetField("privateInnerClass")
    Object getPrivateInnerClass();

    @GetField("staticInnerClass")
    Object getStaticInnerClass();

    @GetField("publicStaticInnerClass")
    Object getPublicStaticInnerClass();

}