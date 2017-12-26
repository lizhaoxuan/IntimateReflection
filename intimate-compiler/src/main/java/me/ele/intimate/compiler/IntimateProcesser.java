package me.ele.intimate.compiler;

import com.google.auto.service.AutoService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
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
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.Method;
import me.ele.intimate.annotation.RefTarget;
import me.ele.intimate.annotation.SetField;
import me.ele.intimate.compiler.model.CName;
import me.ele.intimate.compiler.model.RefFieldModel;
import me.ele.intimate.compiler.model.RefMethodModel;
import me.ele.intimate.compiler.model.RefTargetModel;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */
@AutoService(Processor.class)
public class IntimateProcesser extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;
    private Map<String, RefTargetModel> targetModelMap = new LinkedHashMap<>();
    private Element element;

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

        if (targetModelMap.size() == 0) {
            return true;
        }

        try {
            FileObject fileObject = mFiler.getResource(StandardLocation.CLASS_OUTPUT, "",
                    "intimate/intimate.json");
            mMessager.printMessage(Diagnostic.Kind.OTHER, "output intimate.json dir: " + fileObject.toUri().toString());
            File file = new File(fileObject.toUri());
            Files.createParentDirs(file);
            Writer writer = Files.newWriter(file, Charsets.UTF_8);
            new Gson().toJson(targetModelMap, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> implMap = new LinkedHashMap<>();
        for (RefTargetModel model : targetModelMap.values()) {
            implMap.put(model.getInterfaceName().fullName, model.getImplPackageName() + "." + model.getImplClassName());
            if (model.isSystemClass()) {
                try {
                    new GenerateSystemCode(model).generate().writeTo(mFiler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    new GenerateCode(model).generate().writeTo(mFiler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            new GenerateFactoryCode(implMap).generate().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void processRefTarget(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(RefTarget.class)) {
            if (this.element == null) {
                this.element = element;
            }
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

            List<CName> parameterTypes = getParameterTypes(executableElement);
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
        return SourceVersion.RELEASE_7;
    }
}
