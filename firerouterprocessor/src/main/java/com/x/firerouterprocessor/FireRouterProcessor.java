package com.x.firerouterprocessor;

import com.google.auto.service.AutoService;
import com.x.firerouterannotation.FireRule;

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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class FireRouterProcessor extends AbstractProcessor {

    private Filer filer;//文件编写
    private Messager messager;//消息输出
    private Types types;//处理TypeMirror的工具类

    //存储注解的类信息的列表
    private Map<Integer, FireRulerClass> annotationMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        filer = processingEnvironment.getFiler();

        messager = processingEnvironment.getMessager();

        types = processingEnvironment.getTypeUtils();
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(FireRule.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //清除列表里的所有注解
        annotationMap.clear();
        //获取所有注解
        Set<? extends Element> annotationSet = roundEnvironment.getElementsAnnotatedWith(FireRule.class);
        //循环注解
        FireRouterUtil.loopAnnotation(annotationSet, annotationMap, types, messager);
//        return FireRouterFiler.generateJavaCode(filer, messager, annotationMap);
        return FireRouterFiler.generateCode(filer, messager, annotationMap);
//        return FireRouterFiler.generateStaticJavaCode(filer, messager, annotationMap);
//        return FireRouterFiler.generateStaticCode(filer, messager, annotationMap);
    }
}
