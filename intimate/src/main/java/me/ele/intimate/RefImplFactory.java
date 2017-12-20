package me.ele.intimate;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class RefImplFactory {

    public static <T> T getRefImplThrows(Object object, Class<T> clazz) throws ClassNotFoundException {
        String name = clazz.getCanonicalName();
        return (T) createRefImpl(name);
    }

    public static <T> T getRefImpl(Object object, Class<T> clazz) {
        try {
            return getRefImplThrows(object, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BaseRefImpl createRefImpl(String name) throws ClassNotFoundException {

        return null;
    }
}
