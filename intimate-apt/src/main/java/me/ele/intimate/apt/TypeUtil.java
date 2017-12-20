package me.ele.intimate.apt;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import me.ele.intimate.apt.model.CName;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class TypeUtil {

    public static final ClassName BASE_REF_IMPL = ClassName.get("me.ele.intimate", "BaseRefImpl");
    public static final ClassName INTIMATE_EXCEPTION = ClassName.get("me.ele.intimate", "IntimateException");

    public static String typeDefaultValue(CName cName) {
        if (cName.isPrimitive) {
            if (cName.typeName == TypeName.VOID) return "";
            if (cName.typeName == TypeName.BOOLEAN) return "return false;";
            if (cName.typeName == TypeName.BYTE) return "return 0;";
            if (cName.typeName == TypeName.SHORT) return "return 0;";
            if (cName.typeName == TypeName.INT) return "return 0;";
            if (cName.typeName == TypeName.LONG) return "return 0;";
            if (cName.typeName == TypeName.CHAR) return "return 0;";
            if (cName.typeName == TypeName.FLOAT) return "return 0;";
            if (cName.typeName == TypeName.DOUBLE) return "return 0;";
        } else if (cName.fullName.equals("void")) {
            return "";
        }
        return "return null;";
    }
}
