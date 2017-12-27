package me.ele.intimate;

import android.util.Log;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class RefImplFactory {

    private static IRefImplFactory factoryImpl;
    private static AbsRefFactoryShell factoryShell;

    public static void setFactoryShell(AbsRefFactoryShell factoryShell) {
        RefImplFactory.factoryShell = factoryShell;
    }

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
            //借壳上市，免去寻找Factory的一次反射
            if (factoryShell != null) {
                Log.d("Intimate", "create by factoryShell");
                return factoryShell.createRefImpl(object, name);
            }
            //通过反射创建Factory实例
            if (factoryImpl == null) {
                Log.d("Intimate", "create by factoryImpl");
                factoryImpl = (IRefImplFactory) Class.forName(Constant.I_REF_IMPL_FACTORY_IMPL_FULL_NAME)
                        .newInstance();
            }
            return factoryImpl.createRefImpl(object, name);
        } catch (Exception e) {
            throw new IntimateException(e);
        }
    }
}
