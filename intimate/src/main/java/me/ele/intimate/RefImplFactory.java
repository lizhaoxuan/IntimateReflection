package me.ele.intimate;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class RefImplFactory {

    public static <T> T getRefImplThrows(Object object, Class<T> clazz) throws IntimateException {
        String name = clazz.getCanonicalName();
        return (T) createRefImpl(object, name);
    }

    public static <T> T getRefImpl(Object object, Class<T> clazz) {
        try {
            return getRefImplThrows(object, clazz);
        } catch (IntimateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BaseRefImpl createRefImpl(Object object, String name) throws IntimateException {
        try {
            return RefFactoryImpl.createRefImpl(object, name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
