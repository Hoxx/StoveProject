package com.x.stoveprocessor.findid;

import com.google.auto.service.AutoService;
import com.x.stoveannotation.FindId;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class FireFindIdProcessor extends AbstractProcessor {

    private Types types;
    private Filer filer;
    private Elements elements;
    private Messager messager;


    private Map<String, FireFindIdClass> annotationItemMap = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(FindId.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        types = processingEnvironment.getTypeUtils();
        elements = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        printNodeMessage("FireFindIdProcessor-process...");
        annotationItemMap.clear();
        Set<? extends Element> annotations = roundEnvironment.getElementsAnnotatedWith(FindId.class);
        printNodeMessage("annotations.size():" + annotations.size());
        for (Element element : annotations) {
            if (!isValidAnnotation(element)) {
                printErrorMessage("Annotation type not is true!" + element.getSimpleName());
                return true;
            }
            //获取当前的成员变量
            VariableElement variableElement = (VariableElement) element;
            //类或者接口
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            //完整的名称
            String qualifiedName = typeElement.getQualifiedName().toString();
            //包名
            PackageElement packageElement = elements.getPackageOf(typeElement);

            printNodeMessage("typeElement.getQualifiedName():" + qualifiedName);

            FireFindIdClass item = annotationItemMap.get(qualifiedName);
            if (item == null) {
                item = new FireFindIdClass(types, packageElement, typeElement, messager);
                annotationItemMap.put(qualifiedName, item);
            }

            FindId findId = variableElement.getAnnotation(FindId.class);
            if (findId != null) {
                int id = findId.value();
                item.addAnnotation(id, variableElement);
            }
        }
        printNodeMessage("start create code");
        printNodeMessage("annotationItemMap.size():" + annotationItemMap.size());
        try {
            for (Map.Entry<String, FireFindIdClass> entry : annotationItemMap.entrySet()) {
                entry.getValue().generateJavaCode().writeTo(filer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            printErrorMessage(e.toString());
        }
        return true;
    }

    //检查注解是否有效
    private boolean isValidAnnotation(Element element) {
        if (!element.getKind().isField()) {
            return false;
        }
        return true;
    }

    //输出信息
    private void printErrorMessage(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format("[ @%s --ERROR] " + message, FindId.class.getSimpleName()));
    }

    private void printNodeMessage(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("[ @%s --NOTE] " + message, FindId.class.getSimpleName()));
    }
}
