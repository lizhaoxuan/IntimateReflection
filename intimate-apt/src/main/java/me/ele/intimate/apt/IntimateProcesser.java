package me.ele.intimate.apt;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.Method;
import me.ele.intimate.annotation.RefTarget;
import me.ele.intimate.annotation.SetField;
import me.ele.intimate.apt.model.CName;
import me.ele.intimate.apt.model.RefFieldModel;
import me.ele.intimate.apt.model.RefMethodModel;
import me.ele.intimate.apt.model.RefTargetModel;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */
@AutoService(Processor.class)
public class IntimateProcesser extends AbstractProcessor {

    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Map<String, RefTargetModel> targetModelMap = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        targetModelMap.clear();
        processRefTarget(roundEnvironment);
        processMethod(roundEnvironment);
        processGetField(roundEnvironment);
        processSetField(roundEnvironment);
//        mMessager.printMessage(Diagnostic.Kind.NOTE, new Gson().toJson(targetModelMap));

        for (RefTargetModel model : targetModelMap.values()) {
            if (model.isSystemClass()) {
                try {
                    new GenerateSystemCode(model).generate().writeTo(mFiler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
//                try {
//                    GenerateCode.generate(model).writeTo(mFiler);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }

        return true;
    }

    private void processRefTarget(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(RefTarget.class)) {
            TypeElement classElement = (TypeElement) element;
            RefTarget refTarget = classElement.getAnnotation(RefTarget.class);
            String interfaceFullName = classElement.getQualifiedName().toString();
            RefTargetModel model = new RefTargetModel(interfaceFullName,
                    refTarget.className(),
                    refTarget.needForName(),
                    refTarget.isSystemClass(),
                    refTarget.needThrow());

            targetModelMap.put(interfaceFullName, model);
        }
    }

    private void processMethod(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Method.class)) {
            RefTargetModel targetModel = getTargetModel(element);
            if (targetModel == null) {
                continue;
            }
            ExecutableElement executableElement = (ExecutableElement) element;
            Method method = executableElement.getAnnotation(Method.class);

            //获取参数列表
            List<CName> parameterTypes = getParameterTypes(executableElement);
            //获取name
            String name = method.value();
            if ("".equals(name)) {
                name = executableElement.getSimpleName().toString();
            }

            RefMethodModel methodModel = new RefMethodModel(name,
                    method.needThrow(),
                    executableElement.getReturnType(),
                    parameterTypes);
            targetModel.addMethod(methodModel);
        }
    }

    private void processGetField(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(GetField.class)) {
            RefTargetModel targetModel = getTargetModel(element);
            if (targetModel == null) {
                continue;
            }
            ExecutableElement executableElement = (ExecutableElement) element;
            GetField field = executableElement.getAnnotation(GetField.class);

            RefFieldModel fieldModel = new RefFieldModel(field.value(),
                    executableElement.getSimpleName().toString(),
                    field.needThrow(),
                    executableElement.getReturnType(),
                    false);
            targetModel.addField(fieldModel);
        }

    }

    private void processSetField(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(SetField.class)) {
            RefTargetModel targetModel = getTargetModel(element);
            if (targetModel == null) {
                continue;
            }
            ExecutableElement executableElement = (ExecutableElement) element;
            SetField field = executableElement.getAnnotation(SetField.class);
            //获取参数列表
            List<CName> parameterTypes = getParameterTypes(executableElement);

            RefFieldModel fieldModel = new RefFieldModel(field.value(),
                    executableElement.getSimpleName().toString(),
                    field.needThrow(),
                    executableElement.getReturnType(),
                    true);
            if (parameterTypes.size() > 0) {
                fieldModel.setParameterTypes(parameterTypes.get(0));
            }
            targetModel.addField(fieldModel);
        }
    }

    /**
     * 获取参数列表
     */
    private List<CName> getParameterTypes(ExecutableElement executableElement) {
        List<? extends VariableElement> methodParameters = executableElement.getParameters();
        List<CName> parameterTypes = new ArrayList<>();
        for (VariableElement variableElement : methodParameters) {
            TypeMirror methodParameterType = variableElement.asType();
            if (methodParameterType instanceof TypeVariable) {
                TypeVariable typeVariable = (TypeVariable) methodParameterType;
                methodParameterType = typeVariable.getUpperBound();

            }
            parameterTypes.add(new CName(methodParameterType));
        }
        return parameterTypes;
    }

    private RefTargetModel getTargetModel(Element element) {
        TypeElement classElement = (TypeElement) element
                .getEnclosingElement();
        return targetModelMap.get(classElement.getQualifiedName().toString());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(RefTarget.class.getCanonicalName());
        types.add(Method.class.getCanonicalName());
        types.add(GetField.class.getCanonicalName());
        types.add(SetField.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
