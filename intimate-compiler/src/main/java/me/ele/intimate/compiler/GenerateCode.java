package me.ele.intimate.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import me.ele.intimate.compiler.model.RefFieldModel;
import me.ele.intimate.compiler.model.RefMethodModel;
import me.ele.intimate.compiler.model.RefTargetModel;

import java.util.List;

import javax.lang.model.element.Modifier;

import static me.ele.intimate.compiler.TypeUtil.BASE_REF_IMPL;


/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class GenerateCode {

    RefTargetModel model;

    public GenerateCode(RefTargetModel model) {
        this.model = model;
    }

    public JavaFile generate() {
        MethodSpec.Builder construction = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addException(ClassNotFoundException.class)
                .addParameter(Object.class, "object");
        if (model.getTargetName().fullName.contains("$") || model.isNeedForName()) {
            construction.addStatement("super(object,Class.forName($S))", model.getTargetName().fullName + ".class");
        } else {
            construction.addStatement("super(object,$N.class)", model.getTargetName().fullName);
        }
        construction.addStatement("mData = ($T) mObject", model.getTargetName().typeName);

        TypeSpec.Builder implClass = TypeSpec.classBuilder(model.getImplClassName())
                .addModifiers(Modifier.PUBLIC)
                .superclass(BASE_REF_IMPL)
                .addSuperinterface(model.getInterfaceName().typeName)
                .addMethod(construction.build())
                .addField(model.getTargetName().typeName, "mData");

        generateFiled(implClass, model.getFieldList());
        generateMethod(implClass, model.getMethodList());

        return JavaFile.builder(model.getImplPackageName(), implClass.build()).build();
    }

    private void generateFiled(TypeSpec.Builder implClass, List<RefFieldModel> fieldModelList) {
        if (fieldModelList == null || fieldModelList.size() == 0) {
            return;
        }
        for (RefFieldModel fieldModel : fieldModelList) {
            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(fieldModel.getMethodName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC);
            if (fieldModel.getParameterType() != null) {
                methodSpec.addParameter(fieldModel.getParameterType().typeName, "arg");
            }
            if (!fieldModel.isSet()) {
                methodSpec.returns(fieldModel.getType().typeName);
                methodSpec.addCode(TypeUtil.typeDefaultValue(fieldModel.getType()));
            }
            implClass.addMethod(methodSpec.build());
        }
    }

    private static void generateMethod(TypeSpec.Builder implClass, List<RefMethodModel> methodModels) {
        if (methodModels == null || methodModels.size() == 0) {
            return;
        }
        for (RefMethodModel methodModel : methodModels) {
            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(methodModel.getName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(methodModel.getReturnType().typeName);

            if (methodModel.getParameterTypes() != null && methodModel.getParameterTypes().size() > 0) {
                for (int i = 0; i < methodModel.getParameterTypes().size(); i++) {
                    methodSpec.addParameter(methodModel.getParameterTypes().get(i).typeName, "arg" + i);
                }
            }

            if (!methodModel.isVoid()) {
                methodSpec.addCode(TypeUtil.typeDefaultValue(methodModel.getReturnType()));
            }
            implClass.addMethod(methodSpec.build());
        }
    }


}