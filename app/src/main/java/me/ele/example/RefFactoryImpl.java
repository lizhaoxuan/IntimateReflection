package me.ele.example;

import me.ele.intimate.AbsRefFactoryShell;
import me.ele.intimate.BaseRefImpl;

/**
 * Created by lizhaoxuan on 2017/12/27.
 */

public class RefFactoryImpl extends AbsRefFactoryShell {

    @Override
    public BaseRefImpl createRefImpl(Object object, String name) throws ClassNotFoundException {
        return null;
    }
}
