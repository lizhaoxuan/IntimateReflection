package me.ele.intimate.apt.model;

import java.util.List;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefMethodModel {

    private String name;
    private boolean needThrow;
    private String returnType;
    private List<String> parameterTypes;

    public RefMethodModel(String name, boolean needThrow, String returnType, List<String> parameterTypes) {
        this.name = name;
        this.needThrow = needThrow;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
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

    public List<String> getParameterTypes() {
        return parameterTypes;
    }

}
