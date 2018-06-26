package com.x.firerouterprocessor;

import com.x.firerouterannotation.FireRule;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class FireRouterUtil {

    public static void loopAnnotation(Set<? extends Element> annotationSet, Map<Integer, FireRulerClass> annotationMap, Types types, Messager messager) {
        //循环注解
        printNodeMessage(messager, "start loop annotation--------" + annotationSet.size());

        for (Element element : annotationSet) {

            if (checkAnnotationValid(types, messager, element)) {
                //ElementType.TYPE注解可以直接强转TypeElement
                TypeElement typeElement = (TypeElement) element;

                //根据注解的@Target值，对应的Element,可以直接转换,非对应的Element，通过getEnclosingElement转换获取
                PackageElement packageElement = (PackageElement) element.getEnclosingElement();

                String packageName = packageElement.getQualifiedName().toString();

                printNodeMessage(messager, "start loop annotation packageName:--------" + packageName);
                //全类名
                String fullClassName = typeElement.getQualifiedName().toString();
                printNodeMessage(messager, "start loop annotation fullClassName:--------" + fullClassName);
                //获取注解的值
                FireRule fireRule = typeElement.getAnnotation(FireRule.class);
                if (fireRule != null) {
                    String alias = fireRule.alias();
                    int aliasHashCode = alias.hashCode();
                    //获取FireRulerClass，并判空
                    FireRulerClass rulerClass = annotationMap.get(aliasHashCode);
                    if (rulerClass == null) {
                        rulerClass = new FireRulerClass(alias, aliasHashCode, fullClassName, packageName);
                        annotationMap.put(aliasHashCode, rulerClass);
                    } else {
                        error(messager, element, "There are two same aliases @%s and @%s", rulerClass.getClassFullName(), fullClassName);
                    }
                }
            }
        }
    }


    //检查直接的类型是否正确
    private static boolean checkAnnotationValid(Types types, Messager messager, Element element) {
        //检查被注解为@FireRule的元素是否是一个类
        if (element.getKind() != ElementKind.CLASS) {
            error(messager, element, "Only classes can be annotated with @%s", FireRule.class.getSimpleName());
            return false;
        }
        //根据注解的@Target值，对应的Element,可以直接转换,非对应的Element，通过getEnclosingElement转换获取
        TypeElement typeElement = (TypeElement) element;

        //检查类是否是Public
        if (!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
            error(messager, element, "The class %s is not public.", typeElement.getQualifiedName().toString());
            return false;
        }
        //检查此类是否是抽象类
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            error(messager, element, "The class %s must not be abstract.", typeElement.getQualifiedName().toString());
            return false;
        }

        //检查此类是否是Activity
        if (!isActivity(types, typeElement)) {
            error(messager, element, "The class %s is not activity.", typeElement.getQualifiedName().toString());
            return false;
        }
        return true;
    }

    //判断当前类是否是Activity
    private static boolean isActivity(Types types, TypeElement element) {
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

    //信息输出
    private static void printNodeMessage(Messager messager, String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("[ @%s --NOTE] " + message, FireRule.class.getSimpleName()));
    }

    //错误信息输出
    private static void error(Messager messager, Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
