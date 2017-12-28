package me.ele.intimate.compiler.model;

import java.util.ArrayList;
import java.util.List;

import static me.ele.intimate.Constant.INTIMATE_PACKAGE;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefTargetModel {

    private CName interfaceName;
    private CName targetName;
    private String implPackageName;
    private String implClassName;
    private boolean needForName;
    private boolean needReflection;
    private List<RefFieldModel> fieldList;
    private List<RefMethodModel> methodList;

    public RefTargetModel(String interfaceName, String className, boolean needForName, boolean needReflection) {
        this.interfaceName = new CName(interfaceName);
        this.targetName = new CName(className);
        this.needForName = needForName;
        this.needReflection = needReflection;
        this.implClassName = this.interfaceName.className + "$$Intimate";
        if (needReflection) {
            this.implPackageName = this.targetName.packageName;
        } else {
            this.implPackageName = INTIMATE_PACKAGE;
        }
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

    public boolean isNeedReflection() {
        return needReflection;
    }

    public List<RefFieldModel> getFieldList() {
        return fieldList;
    }

    public List<RefMethodModel> getMethodList() {
        return methodList;
    }

    public String getImplClassName() {
        return implClassName;
    }

    public String getImplPackageName() {
        return implPackageName;
    }
}
