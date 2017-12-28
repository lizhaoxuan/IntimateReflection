package me.ele.intimate.compiler.model;

import java.util.List;

import javax.lang.model.type.TypeMirror;

import me.ele.intimate.compiler.TypeUtil;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefFieldModel {

    private String name;
    private boolean needThrow;
    private String methodName;
    private CName type;
    private CName returnType;
    private boolean isSet;
    private CName parameterType;
    private String methodContentCode;

    public RefFieldModel(String name, String methodName, boolean needThrow, CName type, boolean isSet, TypeMirror returnType) {
        this.name = name;
        this.needThrow = needThrow;
        this.methodName = methodName;
        this.isSet = isSet;
        this.type = type;
        this.returnType = new CName(returnType);
        if (isSet) {
            this.methodContentCode = "{mData." + this.name + " = $1;" + TypeUtil.typeDefaultReturnCode(this.returnType) + "}";
        } else {
            this.methodContentCode = "return mData." + name + ";";
        }
    }

    public CName getReturnType() {
        return returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setParameterTypes(CName parameterTypes) {
        this.parameterType = parameterTypes;
    }

    public String getName() {
        return name;
    }

    public boolean isNeedThrow() {
        return needThrow;
    }

    public CName getType() {
        return type;
    }

    public boolean isSet() {
        return isSet;
    }

    public CName getParameterType() {
        return parameterType;
    }
}
