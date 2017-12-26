package me.ele.intimate.plugin

import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.bytecode.AccessFlag

/**
 * Created by lizhaoxuan on 2017/12/25.
 */

public class ClassInject {

    public static void injectDir(String path, packageIndex, intimateConfig, todoList) {
        println("path:" + path)
        IntimateTransform.pool.appendClassPath(path)
        File dir = new File(path)
        def tempTodoList = []
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath
                //确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {
                    int end = filePath.length() - 6 // .class = 6
                    String className = filePath.substring(packageIndex, end)
                            .replace('/', '.').replace('/', '.')
                    // 判断是否是需要处理的类
                    if (todoList.contains(className)) {
                        processClass(className, path, intimateConfig, todoList)
                        tempTodoList.add(className)
                    }
//                    //从todoList中删除已经处理过的
//                    todoList.minus(tempTodoList)
                }
            }
        }
    }

    private static void processClass(String className, String path,
                                     intimateConfig, todoList) {
        CtClass c = IntimateTransform.pool.getCtClass(className)
        if (c.isFrozen()) {
            c.defrost()
        }

        if (className.contains("\$\$Intimate")) {
            println("processImpl:" + className)
            processImpl(c, intimateConfig)
        } else {
            println("processTarget:" + className)
            processTarget(c, intimateConfig)
        }

        c.writeFile(path)
        c.detach()
    }

    private static void processImpl(CtClass c, intimateConfig) {
        //TODO 未考虑重载函数
        def contentMap = [:]
        def methodList = []
        intimateConfig.each { key, value ->
            for (def filedConfig : value.fieldList) {
                methodList.add(filedConfig.methodName)
                contentMap.put(filedConfig.methodName, filedConfig.methodContentCode)
            }
            //TODO 未考虑重载函数
            for (def methodConfig : value.methodList) {
                methodList.add(methodConfig.name)
                contentMap.put(methodConfig.name, methodConfig.methodContentCode)
            }
        }

        for (CtMethod method : c.getDeclaredMethods()) {
            if (methodList.contains(method.name)) {
                String code = contentMap.get(method.name)
                println("code: " + code)
                method.setBody(code)
            }
        }
    }

    private static void processTarget(CtClass c, intimateConfig) {
        def intimateField = []
        def tempIntimateField = []
        //TODO 暂时只判断了名字，没考虑参数
        def intimateMethod = []
        def tempIntimateMethod = []

        intimateConfig.each { key, value ->
            for (def filedConfig : value.fieldList) {
                intimateField.add(filedConfig.name)
            }
            //TODO 未考虑重载函数
            for (def methodConfig : value.methodList) {
                intimateMethod.add(methodConfig.name)
            }
        }

        for (def filedConfig : intimateConfig.fieldList) {
            intimateField.add(filedConfig.name)
        }

        for (CtField field : c.getDeclaredFields()) {
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
//            ThrowExecutionError.throwError(c.name + " not found field:" + msgFields)
//        }


        for (def methodConfig : intimateConfig.methodList) {
            intimateMethod.add(methodConfig.name)
        }
        for (CtMethod method : c.getDeclaredMethods()) {
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
//            ThrowExecutionError.throwError(c.name + " not found method:" + msgMethods)
//        }
    }

}
