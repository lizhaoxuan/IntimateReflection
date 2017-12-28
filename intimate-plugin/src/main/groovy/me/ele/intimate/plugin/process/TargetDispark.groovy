package me.ele.intimate.plugin.process;

import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.AccessFlag
import me.ele.intimate.plugin.DataSource
import me.ele.intimate.plugin.ThrowExecutionError;

/**
 * Created by lizhaoxuan on 2017/12/28.
 */

class TargetDispark {

    static void processClass(CtClass c) {

        def intimateFieldList = []
        def intimateMethodList = []

        DataSource.intimateConfig.each { key, value ->
            if (key == c.name) {
                for (def filedConfig : value.fieldList) {
                    intimateFieldList.add(GenerateUtils.generateFieldDes(filedConfig))
                }
                for (def methodConfig : value.methodList) {
                    intimateMethodList.add(GenerateUtils.generateMethodDes(methodConfig))
                }
            }
        }

        processTargetField(c, intimateFieldList)
        processTargetMethod(c, intimateMethodList)
    }

    private static void processTargetField(CtClass c, intimateFieldList) {
        def tempIntimateField = []

        for (CtField field : c.getDeclaredFields()) {
            String fieldStr = GenerateUtils.generateFieldDes(field)
            if (intimateFieldList.contains(fieldStr)) {
                field.setModifiers(AccessFlag.setPublic(field.getModifiers()))
                tempIntimateField.add(fieldStr)
            }
        }

        intimateFieldList.removeAll(tempIntimateField)
        if (intimateFieldList.size() != 0) {
            ThrowExecutionError.throwError(c.name + " not found field:" + GenerateUtils.generateNotFoundFieldError(intimateFieldList))
        }
    }

    private static void processTargetMethod(CtClass c, intimateMethodList) {
        def tempIntimateMethod = []

        for (CtMethod method : c.getDeclaredMethods()) {
            String methodInfo = GenerateUtils.generateMethodDes(method)
            if (intimateMethodList.contains(methodInfo)) {
                method.setModifiers(AccessFlag.PUBLIC)
                tempIntimateMethod.add(methodInfo)
            }
        }
        intimateMethodList.removeAll(tempIntimateMethod)
        if (intimateMethodList.size() != 0) {
            ThrowExecutionError.throwError(c.name + " not found method:  " + GenerateUtils.generateNotFoundMethodError(intimateMethodList))
        }
    }

}
