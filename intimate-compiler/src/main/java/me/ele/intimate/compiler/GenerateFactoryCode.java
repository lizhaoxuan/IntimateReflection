package me.ele.intimate.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.Map;

import javax.lang.model.element.Modifier;

import static me.ele.intimate.Constant.INTIMATE_PACKAGE;
import static me.ele.intimate.Constant.IREF_IMPL_FACTORY_CLASS_NAME;
import static me.ele.intimate.compiler.TypeUtil.BASE_REF_IMPL;
import static me.ele.intimate.compiler.TypeUtil.I_REF_IMPL_FACTORY;
import static me.ele.intimate.compiler.TypeUtil.LOG;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class GenerateFactoryCode {

    Map<String, String> implMap;

    public GenerateFactoryCode(Map<String, String> implMap) {
        this.implMap = implMap;
    }

    public JavaFile generate() {
        TypeSpec.Builder implClass = TypeSpec.classBuilder(IREF_IMPL_FACTORY_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(I_REF_IMPL_FACTORY);

        generateMethod(implClass, implMap);
        return JavaFile.builder(INTIMATE_PACKAGE, implClass.build()).build();
    }

    private void generateMethod(TypeSpec.Builder implClass, Map<String, String> implMap) {
        if (implMap == null || implMap.size() == 0) {
            return;
        }
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("createRefImpl")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(BASE_REF_IMPL)
                .addException(ClassNotFoundException.class)
                .addParameter(Object.class, "object")
                .addParameter(String.class, "name");

        for (Map.Entry<String, String> entry : implMap.entrySet()) {
            methodSpec.beginControlFlow("if(name.equals($S))", entry.getKey())
                    .addStatement("System.out.println($S + $S)", "return new ", entry.getValue())
                    .addStatement("return new $N(object)", entry.getValue())
                    .endControlFlow();
        }
        methodSpec.addStatement("$T.w($S,$S + name)", LOG, "Intimate", "not found ");
        methodSpec.addStatement("return null");
        implClass.addMethod(methodSpec.build());
    }

}
