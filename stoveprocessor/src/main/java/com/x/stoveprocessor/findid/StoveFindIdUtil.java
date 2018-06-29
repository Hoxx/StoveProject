package com.x.stoveprocessor.findid;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.x.stoveannotation.FindId;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class StoveFindIdUtil {

    public static void loopAnnotation(Set<? extends Element> annotations, Map<String, StoveFindIdGroup> stoveFindIdGroupMap, Elements elements, Types types, Messager messager) {
        for (Element element : annotations) {
            if (isValidFindIdAnnotation(element, messager)) {
                //获取当前的成员变量
                VariableElement variableElement = (VariableElement) element;
                //类或者接口
                TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
                //完整类名称
                String qualifiedName = typeElement.getQualifiedName().toString();
                //包名
                PackageElement packageElement = elements.getPackageOf(typeElement);

                //根据类名获取对应类的组：每个组对应一个类，且组内部包含类内部的所有注解
                StoveFindIdGroup stoveFindIdGroup = stoveFindIdGroupMap.get(qualifiedName);
                if (stoveFindIdGroup == null) {
                    stoveFindIdGroup = new StoveFindIdGroup(TypeName.get(typeElement.asType()));
                    stoveFindIdGroupMap.put(qualifiedName, stoveFindIdGroup);
                }
                //创建一个单独的注解
                StoveFindIdView findIdView = new StoveFindIdView();
                findIdView.setViewName(variableElement.getSimpleName());
                findIdView.setViewTypeName(ClassName.get(variableElement.asType()));
                findIdView.setFragment(isFragment(typeElement, types));
                //获取注解的值，对应视图的ID
                FindId findId = variableElement.getAnnotation(FindId.class);
                stoveFindIdGroup.addFindIdView(findId.value(), findIdView);
            } else {
                printErrorMessage(messager, "Annotation type not is true!" + element.getSimpleName());
            }
        }
    }

    //检查注解是否有效
    private static boolean isValidFindIdAnnotation(Element element, Messager messager) {
        if (element.getKind() != ElementKind.FIELD) {
            printErrorMessage(messager, "Element must be is field!");
            return false;
        }
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            printErrorMessage(messager, "Filed must be public!");
            return false;
        }
        return true;
    }

    //判断当前类是否是Fragment
    private static boolean isFragment(TypeElement element, Types types) {
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
                return true;
            }
            if (superClass.toString().equals("android.app.Activity")) {
                //判断此类是Activity
                return false;
            }
            //重新把父类设置为当前类
            currentClass = (TypeElement) types.asElement(superClass);
        }
    }

    //输出信息
    private static void printErrorMessage(Messager messager, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format("[ @%s --ERROR] " + message, FindId.class.getSimpleName()));
    }
}
