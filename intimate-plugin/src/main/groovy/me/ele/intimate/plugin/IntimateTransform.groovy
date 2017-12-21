package me.ele.intimate.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import groovy.json.JsonSlurper
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

/**
 * Created by lizhaoxuan on 2017/12/20.
 */

public class IntimateTransform extends Transform {

    Project project
    def intimateConfig = [:]


    public IntimateTransform(Project project) {
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
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        Collection<TransformInput> inputs = transformInvocation.getInputs()
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()
        readIntimateConfig(inputs)

        inputs.each { TransformInput input ->
            //对类型为“文件夹”的input进行遍历
            input.directoryInputs.each { DirectoryInput directoryInput ->
                processClassFile(directoryInput, outputProvider)
            }

            //对类型为jar文件的input进行遍历
            input.jarInputs.each { JarInput jarInput ->
                processJar(jarInput, outputProvider)
            }
        }

        if (intimateConfig.size() > 0) {
            intimateConfig.each { key, value ->
                println(key + " not found")
            }
        }

    }

    private void readIntimateConfig(Collection<TransformInput> inputs) {
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File configFile = new File(directoryInput.file.absolutePath + "/intimate/intimate.json")
                if (configFile.exists()) {
                    def content = new StringBuilder()
                    configFile.eachLine("UTF-8") {
                        content.append(it)
                    }
                    def config = new JsonSlurper().parseText(content.toString())
                    config.each { key, value ->
                        if (!value.isSystemClass) {
                            intimateConfig.put(value.targetName.fullName, value)
                        }
                    }
                    println("size:" + intimateConfig.size())
                    println(intimateConfig)
                }
            }
        }
    }

    private void processClassFile(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        System.out.println("directoryInput.file.absolutePath:" + directoryInput.file.absolutePath)
        File dir = new File(directoryInput.file.absolutePath)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                System.out.println("filePath:" + file.absolutePath)
            }
        }

        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes, directoryInput.scopes,
                Format.DIRECTORY)

        // 将input的目录复制到output指定目录
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    private void processJar(JarInput jarInput, TransformOutputProvider outputProvider) {
        //jar文件一般是第三方依赖库jar文件
        // 重命名输出文件（同目录copyFile会冲突）
        def jarName = jarInput.getName()
        def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
//                println("jarName:" + jarInput.file.getAbsolutePath() + "   md5Name：" + jarName)
        //生成输出路径
        def dest = outputProvider.getContentLocation(jarName + md5Name,
                jarInput.contentTypes, jarInput.scopes, Format.JAR)
        //将输入内容复制到输出
        FileUtils.copyFile(jarInput.file, dest)
    }
}
