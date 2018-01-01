package me.ele.intimate;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class RefImplFactory {

    public static <T> T getRefImpl(Object object, Class<T> clazz) {
        String name = clazz.getCanonicalName();
        return (T) createRefImpl(object, name);
    }

    private static BaseRefImpl createRefImpl(Object object, String name) {
        return RefFactoryImpl.createRefImpl(object, name);
    }
}
