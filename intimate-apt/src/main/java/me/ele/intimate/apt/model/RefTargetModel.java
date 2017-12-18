package me.ele.intimate.apt.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefTarget {

    private String className;
    private String packageName;
    private boolean needForName;
    private boolean isSystemClass;
    private boolean needThrow;
    List<RefField> fieldList;
    List<RefMethod> methodList;

    public RefTarget(String className, boolean needForName, boolean isSystemClass, boolean needThrow) {
        this.className = className;
        this.needForName = needForName;
        this.isSystemClass = isSystemClass;
        this.needThrow = needThrow;
        String[] temp = className.split(".");
        StringBuilder builder = new StringBuilder(temp[0]);
        if (temp.length > 2) {
            for (int i = 1; i < temp.length - 1; i++) {
                builder.append(temp[i]);
                if (i != temp.length - 2) {
                    builder.append('.');
                }
            }
        }
        this.packageName = builder.toString();
    }

    public void addField(RefField field) {
        if (fieldList == null) {
            fieldList = new ArrayList<>();
        }
        fieldList.add(field);
    }

    public void addMethodList(RefMethod method) {
        if (methodList == null) {
            methodList = new ArrayList<>();
        }
        methodList.add(method);
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isNeedForName() {
        return needForName;
    }

    public boolean isSystemClass() {
        return isSystemClass;
    }

    public boolean isNeedThrow() {
        return needThrow;
    }

    public List<RefField> getFieldList() {
        return fieldList;
    }

    public List<RefMethod> getMethodList() {
        return methodList;
    }
}
