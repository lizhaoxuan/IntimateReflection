package me.ele.intimate.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import groovy.json.JsonSlurper
import javassist.ClassPool
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project


/**
 * Created by lizhaoxuan on 2017/12/20.
 */

class IntimateTransform extends Transform {

    static ClassPool pool = ClassPool.getDefault()
    Project project

    IntimateTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "Intimate"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        Collection<TransformInput> inputs = transformInvocation.getInputs()
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()
        readIntimateConfig(inputs)

        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                JarInject.insertClassPath(jarInput.file.absolutePath)
            }

            input.jarInputs.each { JarInput jarInput ->
                processJar(jarInput, outputProvider)
            }

            input.directoryInputs.each { DirectoryInput directoryInput ->
                processClassFile(directoryInput, outputProvider)
            }
        }

        if (DataSource.todoList.size() != 0) {
            ThrowExecutionError.throwError(" not found Class:  " + me.ele.intimate.plugin.process.GenerateUtils.generateNotFoundClassError(DataSource.todoList))
        }

    }

    private static void readIntimateConfig(Collection<TransformInput> inputs) {
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File configFile = new File(directoryInput.file.absolutePath + "/intimate/intimate.json")
                if (configFile.exists()) {
                    def content = new StringBuilder()
                    configFile.eachLine("UTF-8") {
                        content.append(it)
                    }
                    def data = new JsonSlurper().parseText(content.toString())

                    if (data.refFactoryShellName != null && data.refFactoryShellName != "") {
                        DataSource.refFactoryShellName = data.refFactoryShellName
                        DataSource.todoList.add(data.refFactoryShellName)
                    }

                    data.targetModelMap.each { key, value ->
                        DataSource.implMap.put(value.interfaceName.fullName, value.implPackageName + "." + value.implClassName)
                        if (!value.isSystemClass) {
                            DataSource.intimateConfig.put(value.targetName.fullName, value)
                            if (!DataSource.todoList.contains(value.implPackageName + "." + value.implClassName)) {
                                DataSource.todoList.add(value.implPackageName + "." + value.implClassName)
                            }
                            if (!DataSource.todoList.contains(value.targetName.fullName)) {
                                DataSource.todoList.add(value.targetName.fullName)
                            }
                        }
                    }
                }
            }
        }
    }

    private
    static void processClassFile(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        int packageIndex = directoryInput.file.absolutePath.toString().length() + 1
        ClassInject.injectDir(directoryInput.file.absolutePath, packageIndex)

        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes, directoryInput.scopes,
                Format.DIRECTORY)

        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    private static void processJar(JarInput jarInput, TransformOutputProvider outputProvider) {
        def jarName = jarInput.name
        def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4)
        }
        def dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)

        String jarPath = jarInput.file.absolutePath
        if (jarPath.endsWith(".jar")) {
            JarInject.injectJar(jarPath)
        }

        FileUtils.copyFile(jarInput.file, dest)
    }
}
