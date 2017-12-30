package me.ele.example.test.support;

import me.ele.example.mock.CarProtected;
import me.ele.intimate.annotation.RefTarget;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */
@RefTarget(clazz = CarProtected.class, optimizationRef = true)
public interface RefCarProtected {
}
