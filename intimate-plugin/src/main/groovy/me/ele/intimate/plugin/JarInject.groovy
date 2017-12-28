package me.ele.intimate.plugin

import javassist.CtClass
import javassist.NotFoundException
import me.ele.intimate.plugin.process.TargetDispark

class JarInject {

    static void insertClassPath(String path) {
        if (path.endsWith(".jar")) {
            IntimateTransform.pool.insertClassPath(path)
        }
    }

    /**
     * 这里需要将jar包先解压，注入代码后再重新生成jar包
     * @path jar包的绝对路径
     */
    static void injectJar(String path) {
        if (path.endsWith(".jar")) {
            // 注入代码
            def tempTodoList = []
            for (String className : DataSource.todoList) {
                try {
                    CtClass ctClass = IntimateTransform.pool.getCtClass(className)
                    processClass(ctClass)
                    tempTodoList.add(className)
                } catch (NotFoundException e) {
                    //println(path + "notFound factory")
                }
            }
            //从todoList中删除已经处理过的
            DataSource.todoList.removeAll(tempTodoList)
        }
    }

    private static void processClass(CtClass c) {
        if (c.isFrozen()) {
            c.defrost()
        }

        TargetDispark.processClass(c)

        c.writeFile()
        c.detach()
    }
}