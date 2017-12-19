package me.ele.intimate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class BaseRefImpl {

    protected Object mObject;
    protected Class mClass;

    public BaseRefImpl(Object mObject, Class mClass) {
        this.mObject = mObject;
        this.mClass = mClass;
    }

    protected Method getMethod(String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = mClass.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    protected Field getField(String fieldName) throws NoSuchFieldException {
        Field field = mClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
}
