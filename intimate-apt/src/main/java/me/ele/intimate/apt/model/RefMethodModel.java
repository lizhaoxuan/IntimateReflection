package me.ele.intimate.apt.model;

import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefMethodModel {

    private String name;
    private boolean needThrow;
    private CName returnType;
    private List<CName> parameterTypes;

    public RefMethodModel(String name, boolean needThrow, TypeMirror returnType, List<CName> parameterTypes) {
        this.name = name;
        this.needThrow = needThrow;
        this.returnType = new CName(returnType);
        this.parameterTypes = parameterTypes;
    }

    public String getName() {
        return name;
    }

    public boolean isNeedThrow() {
        return needThrow;
    }

    public CName getReturnType() {
        return returnType;
    }

    public List<CName> getParameterTypes() {
        return parameterTypes;
    }

    public boolean isVoid() {
        return returnType.className.equals("");
    }

}
