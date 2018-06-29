package com.x.stoveprocessor.findid;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.x.stoveannotation.FindId;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/*对应的是一个类，如果在一个类中多个地方用到了这个注解，那么会把左右的注解信息，加入到这个annotationMapInfo里面*/
public class FireFindIdClass {

    private Types types;
    private TypeElement typeElement;
    private Messager messager;

    private final static String interfacePackageName = "com.x.stoveinject";
    private final static String interfaceName = "StoveFindIdInterface";

    private final static String generateFileName = "$$StoveInject";
    private String generateFilePackageName;

    //判断当前类是否是Fragment
    private boolean isFragment;

    private Map<Integer, VariableElement> annotationMapInfo = new HashMap<>();

    public FireFindIdClass(Types types, PackageElement packageElement, TypeElement typeElement, Messager messager) {
        this.messager = messager;
        this.types = types;
        this.typeElement = typeElement;
        isFragment = isFragment(typeElement);
        generateFilePackageName = packageElement.getQualifiedName().toString();
    }

    //收集一个类中的所有该注解的变量信息
    public void addAnnotation(int id, VariableElement element) {
        if (!annotationMapInfo.containsKey(id)) {
            annotationMapInfo.put(id, element);
        }
    }

    //判断当前类是否是Fragment
    private boolean isFragment(TypeElement element) {
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

    //生成代码
    public JavaFile generateJavaCode() {
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("findViewById")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(Void.TYPE)
                .addParameter(TypeName.get(typeElement.asType()), "host", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "source");
        printNodeMessage("Create Method!");
        /* $T:代表的是一个类型，不能用String替换,$N:代表的是字符串资源 $L:代表的是long型 */
        for (Map.Entry<Integer, VariableElement> entry : annotationMapInfo.entrySet()) {
            if (isFragment) {
                methodSpec.addStatement("host.$N = ($T)((android.view.View)source).findViewById($L)",
                        entry.getValue().getSimpleName().toString(),
                        ClassName.get(entry.getValue().asType()),
                        entry.getKey());
            } else {
                methodSpec.addStatement("host.$N = ($T)host.findViewById($L)",
                        entry.getValue().getSimpleName().toString(),
                        ClassName.get(entry.getValue().asType()),
                        entry.getKey());
            }
        }
        printNodeMessage("Create Class!");
        TypeSpec typeSpec = TypeSpec.classBuilder(typeElement.getSimpleName().toString() + generateFileName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                ClassName.get(interfacePackageName, interfaceName),
                                TypeName.get(typeElement.asType())
                        )
                )
                .addMethod(methodSpec.build())
                .build();
        printNodeMessage("Create Package!");
        return JavaFile.builder(generateFilePackageName, typeSpec).build();
    }

    //输出信息
    private void printErrorMessage(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format("[ @%s --ERROR] " + message, FindId.class.getSimpleName()));
    }

    private void printNodeMessage(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("[ @%s --NOTE] " + message, FindId.class.getSimpleName()));
    }
}
