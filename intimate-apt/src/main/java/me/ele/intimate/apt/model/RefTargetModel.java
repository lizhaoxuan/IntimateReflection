package me.ele.intimate.apt.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefTargetModel {

    //描述反射需求的接口
    private CName interfaceName;
    private CName targetName;
    private boolean needForName;
    private boolean isSystemClass;
    private boolean needThrow;
    List<RefFieldModel> fieldList;
    List<RefMethodModel> methodList;

    public RefTargetModel(String interfaceName, String className, boolean needForName, boolean isSystemClass, boolean needThrow) {
        this.interfaceName = new CName(interfaceName);
        this.targetName = new CName(className);
        this.needForName = needForName;
        this.isSystemClass = isSystemClass;
        this.needThrow = needThrow;
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

    public CName getInterfaceName() {
        return interfaceName;
    }

    public CName getTargetName() {
        return targetName;
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
