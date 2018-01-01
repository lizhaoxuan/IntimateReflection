package me.ele.example.ref;

import me.ele.example.mock.InnerClazz;
import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.RefTarget;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */
@RefTarget(clazz = InnerClazz.PublicStaticInnerClass.class, optimizationRef = true)
public interface RefPublicStaticInnerClass {

    @GetField("name")
    String getName();
}
