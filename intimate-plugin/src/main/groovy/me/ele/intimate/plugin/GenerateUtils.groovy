package me.ele.intimate.plugin

import javassist.CtClass
import javassist.CtField
import javassist.CtMethod;

/**
 * Created by lizhaoxuan on 2017/12/28.
 */

class GenerateUtils {

    static String generateCreateRefImplCode(implMap) {
        StringBuilder builder = new StringBuilder()
        implMap.each { key, value ->
            builder.append("if(\$2.equals(\"").append(key).append("\")){ ")
                    .append("return new ").append(value).append("(\$1);} \n")
        }
        return builder.toString()
    }

    static String generateFieldInfo(def fieldConfig) {
        return "[" + fieldConfig.type.fullName + " : " + fieldConfig.name + "]"
    }

    static String generateMethodInfo(def methodConfig) {
        StringBuilder me = new StringBuilder(methodConfig.name).append("(")
        for (def parameterTypes : methodConfig.parameterTypes) {
            me.append(parameterTypes.fullName).append(",")
        }
        if (me.lastIndexOf(",") == me.length() - 1) {
            me.deleteCharAt(me.length() - 1)
        }
        me.append(")")
        return me.toString()
    }

    static String generateFieldInfo(CtField field) {
        return "[" + field.getType().name + " : " + field.name + "]"
    }

    static String generateMethodInfo(CtMethod method) {
        StringBuilder me = new StringBuilder(method.name).append("(")
        for (CtClass parameterType : method.getParameterTypes()) {
            me.append(parameterType.name).append(",")
        }
        if (me.lastIndexOf(",") == me.length() - 1) {
            me.deleteCharAt(me.length() - 1)
        }
        me.append(")")
        return me.toString()

    }

    static String generateNotFoundFieldError(def intimateFieldList) {
        StringBuilder msg = new StringBuilder()
        for (String str : intimateFieldList) {
            msg.append(str).append(" \n")
        }
        return msg.toString()
    }

    public static String generateNotFoundMethodError(def intimateMethod) {
        StringBuilder msg = new StringBuilder()
        for (String str : intimateMethod) {
            msg.append(str).append(" \n")
        }
        return msg.toString()
    }
}
