package me.ele.example.ref;

import me.ele.intimate.annotation.Method;
import me.ele.intimate.annotation.RefTargetForName;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */
@RefTargetForName(className = "me.ele.example.mock.InnerClazz$PrivateInnerClass", needForName = true, optimizationRef = true)
public interface RefPrivateInnerClass {

    @Method
    String getName();

}
