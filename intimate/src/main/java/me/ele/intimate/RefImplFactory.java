package me.ele.intimate;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class RefImplFactory {

    private static IRefImplFactory factoryImpl;

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
            if (factoryImpl == null) {
                factoryImpl = (IRefImplFactory) Class.forName(Constant.I_REF_IMPL_FACTORY_IMPL_FULL_NAME)
                        .newInstance();
            }
            return factoryImpl.createRefImpl(object, name);
        } catch (Exception e) {
            throw new IntimateException(e);
        }
    }
}
