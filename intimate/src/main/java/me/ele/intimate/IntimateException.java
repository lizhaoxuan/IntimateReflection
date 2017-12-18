package me.ele.intimate;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class IntimateException extends Exception {
    public final int type_classNotFoundException = 1;
    public final int type_illegalAccessException = 2;
    public final int type_instantiationException = 3;
    public final int type_noSuchMethodException = 4;
    public final int type_invocationTargetException = 5;

    private int exceptionType;

    public IntimateException(Exception e) {
        super(e);
        if (e instanceof ClassNotFoundException) {
            this.exceptionType = type_classNotFoundException;
        } else if (e instanceof IllegalAccessException) {
            this.exceptionType = type_illegalAccessException;
        } else if (e instanceof InstantiationException) {
            this.exceptionType = type_instantiationException;
        } else if (e instanceof NoSuchMethodException) {
            this.exceptionType = type_noSuchMethodException;
        } else if (e instanceof InvocationTargetException) {
            this.exceptionType = type_invocationTargetException;
        }
    }

    public boolean isClassNotFoundException() {
        return exceptionType == type_classNotFoundException;
    }

    public boolean isIllegalAccessException() {
        return exceptionType == type_illegalAccessException;
    }

    public boolean isInstantiationException() {
        return exceptionType == type_instantiationException;
    }

    public boolean isNoSuchMethodException() {
        return exceptionType == type_noSuchMethodException;
    }

    public boolean isInvocationTargetException() {
        return exceptionType == type_invocationTargetException;
    }

    public int getExceptionType() {
        return exceptionType;
    }
}
