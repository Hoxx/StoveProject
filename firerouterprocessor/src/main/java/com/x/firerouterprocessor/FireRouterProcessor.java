package com.x.firerouterprocessor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.x.firerouterannotation.FireRule;

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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

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
        printNodeMessage("searching annotation--------");
        Set<? extends Element> annotationSet = roundEnvironment.getElementsAnnotatedWith(FireRule.class);
        //循环注解
        printNodeMessage("start loop annotation--------");
        for (Element element : annotationSet) {
            if (checkAnnotationValid(element)) {
                //ElementType.TYPE注解可以直接强转TypeElement
                TypeElement typeElement = (TypeElement) element;
                //根据注解的@Target值，对应的Element,可以直接转换,非对应的Element，通过getEnclosingElement转换获取
                PackageElement packageElement = (PackageElement) element.getEnclosingElement();

                //全类名
                String fullClassName = typeElement.getQualifiedName().toString();
                printNodeMessage("start loop annotation fullClassName:--------" + fullClassName);
                //获取注解的值
                FireRule fireRule = typeElement.getAnnotation(FireRule.class);
                if (fireRule != null) {
                    String alias = fireRule.alias();
                    int aliasHashCode = alias.hashCode();
                    //获取FireRulerClass，并判空
                    FireRulerClass rulerClass = annotationMap.get(aliasHashCode);
                    if (rulerClass == null) {
                        rulerClass = new FireRulerClass(alias, aliasHashCode, fullClassName);
                        annotationMap.put(aliasHashCode, rulerClass);
                    } else {
                        error(element, "There are two identical aliases @%s and @%s", rulerClass.getClassFullName(), fullClassName);
                        return true;
                    }
                }
            }
        }
        if (!annotationMap.isEmpty()) {
            printNodeMessage("start generate Java Code--------");

            JavaFile javaFile = generateJavaCode();
            if (javaFile == null) {
                error(null, "FireRule annotation is never used");
                return true;
            } else {
                try {
                    javaFile.writeTo(filer);
                } catch (IOException e) {
                    error(null, "javaFile write error @%s", e.toString());
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    //
/* 生成代码:样式
public class $_$FireRuler implements FireRulerInterface {

    static String FIRE_RULER_INSTANCE_CLASS_NAME = "$_$FireRuler";

    static String FIRE_RULER_INSTANCE_PACKAGE_NAME = "com.x.firerouter";

    @Override
    public Class<?> getAlias(String alias) throws ClassNotFoundException {
        switch (alias.hashCode()) {
            case 100://"other"
                return Class.forName("com.x.stove.otherMainActivity");
        }
        return null;
    }
}
*/
    private JavaFile generateJavaCode() {
        if (annotationMap.isEmpty()) return null;
        printNodeMessage("Create MethodSpec!");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getAlias")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(Class.class)
                .addParameter(TypeName.get(String.class), "alias")
                .addException(TypeName.get(ClassNotFoundException.class));

        printNodeMessage("Create CodeBlock!");

        methodBuilder.beginControlFlow("switch (alias.hashCode())");
        for (Map.Entry<Integer, FireRulerClass> entry : annotationMap.entrySet()) {
            FireRulerClass fireRulerClass = entry.getValue();
            methodBuilder.addStatement("case $L:\n $> return Class.forName($S) $<", fireRulerClass.getAliasHashCode(), fireRulerClass.getClassFullName());
        }
        methodBuilder.endControlFlow();
        methodBuilder.addStatement("return null");
        printNodeMessage("Create MethodSpec end!");
        //创建类
        printNodeMessage("Create Class!");
        TypeSpec typeSpec = TypeSpec.classBuilder("$$FireRuler")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get("com.x.firerouter", "FireRulerInterface"))
                .addMethod(methodBuilder.build())
                .build();
        //输入包名，与Class的参数，返回生成代码的Filer
        return JavaFile.builder("com.x.firerouter", typeSpec).build();
    }

    //检查直接的类型是否正确
    private boolean checkAnnotationValid(Element element) {
        //检查被注解为@FireRule的元素是否是一个类
        if (element.getKind() != ElementKind.CLASS) {
            error(element, "Only classes can be annotated with @%s", FireRule.class.getSimpleName());
            return false;
        }

        //根据注解的@Target值，对应的Element,可以直接转换,非对应的Element，通过getEnclosingElement转换获取
        TypeElement typeElement = (TypeElement) element;

        //检查类是否是Public
        if (!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
            error(element, "The class %s is not public.", typeElement.getQualifiedName().toString());
            return false;
        }
        //检查此类是否是抽象类
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            error(element, "The class %s must not be abstract.", typeElement.getQualifiedName().toString());
            return false;
        }

        //检查此类是否是Activity
        if (!isActivity(typeElement)) {
            error(element, "The class %s is not activity.", typeElement.getQualifiedName().toString());
            return false;
        }
        return true;
    }

    //判断当前类是否是Activity
    private boolean isActivity(TypeElement element) {
        TypeElement currentClass = element;
        //获取当前父类
        while (true) {
            TypeMirror superClass = currentClass.getSuperclass();
            if (superClass.getKind() == TypeKind.NONE) {
                //父类是基础类型Object
                return false;
            }
            if (superClass.toString().equals("android.support.v4.app.Fragment")) {
                //判断此类是Fragment
                return false;
            }
            if (superClass.toString().equals("android.app.Activity")) {
                //判断此类是Activity
                return true;
            }
            //重新把父类设置为当前类
            currentClass = (TypeElement) types.asElement(superClass);
        }
    }

    //错误信息输出
    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    private void printNodeMessage(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("[ @%s --NOTE] " + message, FireRule.class.getSimpleName()));
    }
}
