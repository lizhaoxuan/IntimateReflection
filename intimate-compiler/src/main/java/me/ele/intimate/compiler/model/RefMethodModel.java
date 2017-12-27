package me.ele.intimate.compiler.model;

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
    private String methodContentCode;

    public RefMethodModel(String name, boolean needThrow, TypeMirror returnType, List<CName> parameterTypes) {
        this.name = name;
        this.needThrow = needThrow;
        this.returnType = new CName(returnType);
        this.parameterTypes = parameterTypes;
        this.methodContentCode = generateMethodContentCode();
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

    private String generateMethodContentCode() {
        StringBuilder builder = new StringBuilder();
        if (this.returnType.fullName.equals("void")) {
            builder.append("mData.").append(name).append("(");
        } else {
            builder.append("return mData.").append(name).append("(");
        }
        if (parameterTypes != null && parameterTypes.size() > 0) {
            for (int i = 1; i <= parameterTypes.size(); i++) {
                builder.append("$").append(i);
                if (i != parameterTypes.size()) {
                    builder.append(",");
                }
            }
        }
        builder.append(");");
        return builder.toString();
    }

}
