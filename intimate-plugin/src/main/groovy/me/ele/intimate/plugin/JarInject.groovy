package me.ele.intimate.plugin

import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.NotFoundException
import javassist.bytecode.AccessFlag

public class JarInject {

    static void insertClassPath(String path) {
        if (path.endsWith(".jar")) {
            IntimateTransform.pool.insertClassPath(path)
        }
    }

    /**
     * 这里需要将jar包先解压，注入代码后再重新生成jar包
     * @path jar包的绝对路径
     */
    public static void injectJar(String path, intimateConfig, todoList) {
        if (path.endsWith(".jar")) {
            // 注入代码
            for (String className : todoList) {
                try {
                    CtClass ctClass = IntimateTransform.pool.getCtClass(className)
                    processClass(ctClass, intimateConfig)
                } catch (NotFoundException e) {
//                    println(path + "notFound factory")
                }
            }
        }
    }

    private static void processClass(CtClass c, intimateConfig) {
        if (c.isFrozen()) {
            c.defrost()
        }

        processFields(c.getDeclaredFields(), intimateConfig, c.name)
        processMethods(c.getDeclaredMethods(), intimateConfig, c.name)

        c.writeFile()
        c.detach()
    }

    private static void processFields(CtField[] ctFields, intimateConfig, className) {
        def intimateField = []
        def tempIntimateField = []
        intimateConfig.each { key, value ->
            for (def filedConfig : value.fieldList) {
                intimateField.add(filedConfig.name)
            }
        }

        for (CtField field : ctFields) {
            println("field.name：" + field.name)
            if (intimateField.contains(field.name)) {
                println("Modifiers : " + field.name)
                field.setModifiers(AccessFlag.PUBLIC)
                tempIntimateField.add(field.name)
            }
        }
//        intimateField.removeAll(tempIntimateField)
//        if (intimateField.size() != 0) {
//            String msgFields = ""
//            for (String str : intimateField) {
//                msgFields += str + "  "
//            }
//            ThrowExecutionError.throwError(className + " not found field:" + msgFields)
//        }
    }

    private static void processMethods(CtMethod[] ctMethods, intimateConfig, String className) {
        //TODO 暂时只判断了名字，没考虑参数
        def intimateMethod = []
        def tempIntimateMethod = []
        intimateConfig.each { key, value ->
            for (def methodConfig : intimateConfig.methodList) {
                intimateMethod.add(methodConfig.name)
            }
        }

        for (CtMethod method : ctMethods) {
            if (intimateMethod.contains(method.name)) {
                method.setModifiers(AccessFlag.PUBLIC)
                tempIntimateMethod.add(method.name)
            }
        }

//        intimateMethod.removeAll(tempIntimateMethod)
//        if (intimateMethod.size() != 0) {
//            String msgMethods = ""
//            for (String str : intimateMethod) {
//                msgMethods += str + "  "
//            }
//            ThrowExecutionError.throwError(className + " not found method:" + msgMethods)
//        }
    }
}