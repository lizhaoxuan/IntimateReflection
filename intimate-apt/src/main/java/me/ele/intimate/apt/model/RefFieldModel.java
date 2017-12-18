package me.ele.intimate.apt.model;

import java.util.List;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefFieldModel {

    private String name;
    private boolean needThrow;
    private String type;
    private boolean isSet;
    private List<String> parameterTypes;

    public RefFieldModel(String name, boolean needThrow, String type, boolean isSet) {
        this.name = name;
        this.needThrow = needThrow;
        this.type = type;
        this.isSet = isSet;
    }

    public void setParameterTypes(List<String> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String getName() {
        return name;
    }

    public boolean isNeedThrow() {
        return needThrow;
    }

    public String getType() {
        return type;
    }

    public boolean isSet() {
        return isSet;
    }

    public List<String> getParameterTypes() {
        return parameterTypes;
    }
}
