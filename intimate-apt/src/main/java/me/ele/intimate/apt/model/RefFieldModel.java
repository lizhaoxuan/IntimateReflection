package me.ele.intimate.apt.model;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefField {

    private String name;
    private boolean needThrow;
    private String type;
    private boolean isSet;

    public RefField(String name, boolean needThrow, String type, boolean isSet) {
        this.name = name;
        this.needThrow = needThrow;
        this.type = type;
        this.isSet = isSet;
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
}
