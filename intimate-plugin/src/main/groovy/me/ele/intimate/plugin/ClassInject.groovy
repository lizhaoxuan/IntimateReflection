package me.ele.intimate.plugin

import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.bytecode.AccessFlag

/**
 * Created by lizhaoxuan on 2017/12/25.
 */

public class ClassInject {

    public static void injectDir(String path, packageIndex) {
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
                    if (DataSource.todoList.contains(className)) {
                        processClass(className, path)
                        tempTodoList.add(className)
                    }
//                    //从todoList中删除已经处理过的
//                    todoList.minus(tempTodoList)
                }
            }
        }
    }


    private static void processClass(String className, String path) {
        CtClass c = IntimateTransform.pool.getCtClass(className)
        if (c.isFrozen()) {
            c.defrost()
        }

        if (DataSource.refFactoryShellName == className) {
            println("processFactory:" + className)
            processFactory(c)
        } else if (className.contains("\$\$Intimate")) {
            println("processImpl:" + className)
            processImpl(c)
        } else {
            println("processTarget:" + className)
            processTarget(c)
        }

        c.writeFile(path)
        c.detach()
    }

    private static void processFactory(CtClass c) {
        if (DataSource.implMap.size() == 0) {
            return
        }
        CtMethod ctMethods = c.getDeclaredMethod("createRefImpl")
        String code = GenerateUtils.generateCreateRefImplCode(DataSource.implMap)
        ctMethods.insertBefore(code)
    }

    private static void processImpl(CtClass c) {
        def contentMap = [:]
        def methodList = []
        DataSource.intimateConfig.each { key, value ->
            for (def filedConfig : value.fieldList) {
                String des = GenerateUtils.generateImplFieldDes(filedConfig)
                methodList.add(des)
                contentMap.put(des, filedConfig.methodContentCode)
            }

            for (def methodConfig : value.methodList) {
                String des = GenerateUtils.generateImplMethodDes(methodConfig)
                methodList.add(des)
                contentMap.put(des, methodConfig.methodContentCode)
            }
        }

        for (CtMethod method : c.getDeclaredMethods()) {
            if (methodList.contains(method.name)) {
                String code = contentMap.get(method.name)
                method.setBody(code)
            }
        }
    }

    private static void processTarget(CtClass c) {
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
