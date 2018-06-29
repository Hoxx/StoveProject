package com.x.stoveprocessor.findid;

import com.google.auto.service.AutoService;
import com.x.stoveannotation.FindId;

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
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class StoveFindIdProcessor extends AbstractProcessor {

    private Types types;
    private Filer filer;
    private Elements elements;
    private Messager messager;


    private Map<String, StoveFindIdGroup> findIdGroupMap = new HashMap<>();

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
        //清空类名对应的Map
        findIdGroupMap.clear();
        //扫描所有注解
        Set<? extends Element> annotations = roundEnvironment.getElementsAnnotatedWith(FindId.class);
        //循环注解
        StoveFindIdUtil.loopAnnotation(annotations, findIdGroupMap, elements, types, messager);
        return StoveFindIdFiler.generateJavaCode(findIdGroupMap, filer, messager);
    }

}
