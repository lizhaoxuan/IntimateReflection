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
    public static void injectJar(String path) {
        if (path.endsWith(".jar")) {
            // 注入代码
            for (String className : DataSource.todoList) {
                try {
                    CtClass ctClass = IntimateTransform.pool.getCtClass(className)
                    processClass(ctClass)
                } catch (NotFoundException e) {
//                    println(path + "notFound factory")
                }
            }
        }
    }

    private static void processClass(CtClass c) {
        if (c.isFrozen()) {
            c.defrost()
        }

        processFields(c.getDeclaredFields(), c.name)
        processMethods(c.getDeclaredMethods(), c.name)

        c.writeFile()
        c.detach()
    }

    private static void processFields(CtField[] ctFields, className) {
        def intimateField = []
        def tempIntimateField = []
        DataSource.intimateConfig.each { key, value ->
            for (def filedConfig : value.fieldList) {
                intimateField.add(filedConfig.name)
            }
        }

        for (CtField field : ctFields) {
            if (intimateField.contains(field.name)) {
                field.setModifiers(AccessFlag.setPublic(field.getModifiers()))
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

    private static void processMethods(CtMethod[] ctMethods, String className) {
        //TODO 暂时只判断了名字，没考虑参数
        def intimateMethod = []
        def tempIntimateMethod = []
        DataSource.intimateConfig.each { key, value ->
            for (def methodConfig : value.methodList) {
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