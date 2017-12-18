package me.ele.intimate.apt.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefTargetModel {

    //描述反射需求的接口
    private String interfaceName;
    private String name;
    private String fullName;
    private String packageName;
    private boolean needForName;
    private boolean isSystemClass;
    private boolean needThrow;
    List<RefFieldModel> fieldList;
    List<RefMethodModel> methodList;

    public RefTargetModel(String interfaceName, String className, boolean needForName, boolean isSystemClass, boolean needThrow) {
        this.interfaceName = interfaceName;
        this.fullName = className;
        this.needForName = needForName;
        this.isSystemClass = isSystemClass;
        this.needThrow = needThrow;
        String[] temp = className.split(".");
        this.name = temp[temp.length - 1];
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

    public String getInterfaceName() {
        return interfaceName;
    }

    public void addField(RefFieldModel field) {
        if (fieldList == null) {
            fieldList = new ArrayList<>();
        }
        fieldList.add(field);
    }

    public void addMethod(RefMethodModel method) {
        if (methodList == null) {
            methodList = new ArrayList<>();
        }
        methodList.add(method);
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
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

    public List<RefFieldModel> getFieldList() {
        return fieldList;
    }

    public List<RefMethodModel> getMethodList() {
        return methodList;
    }
}
