package me.ele.intimate.apt.model;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefMethod {

    private String name;
    private boolean needThrow;
    private String returnType;
    private String[] parameterTypes;
    private boolean isSet;

    public RefMethod(String name, boolean needThrow, String returnType, String[] parameterTypes, boolean isSet) {
        this.name = name;
        this.needThrow = needThrow;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.isSet = isSet;
    }

    public String getName() {
        return name;
    }

    public boolean isNeedThrow() {
        return needThrow;
    }

    public String getReturnType() {
        return returnType;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public boolean isSet() {
        return isSet;
    }
}
