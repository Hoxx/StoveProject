package com.x.stoveprocessor.router;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.x.stoveannotation.RouterRule;
import com.x.stoveannotation.StoveConstant;

import java.io.IOException;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;

public class FireRouterFiler {

    /* 生成代码:样式
    * public class $$FireRuler + PackageName implements StoveRuleInterface {

        @Override
        public void StoveRuleInterface(SparseArray<String> stoveRuleMap) {
            stoveRuleMap.put(1136912392,"com.x.mainapp.MainActivity");
            stoveRuleMap.put(1812103677,"com.x.mainapp.info.InfoActivity");
        }

      }
    */
    public static boolean generateCode(Filer filer, Messager messager, Map<Integer, FireRulerClass> annotationMap) {
        if (annotationMap.isEmpty()) return false;

        printNodeMessage(messager, "Create Override Method Param!");
        //Override Method Param
        ClassName className = ClassName.get("android.util", "SparseArray");
        TypeName paramType = ParameterizedTypeName.get(className, TypeName.get(String.class));
        ParameterSpec parameterSpec = ParameterSpec.builder(paramType, "stoveRuleMap").build();
        printNodeMessage(messager, "Create Method!");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("addStoveRuleMap")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(parameterSpec);
        //generate File Name
        String javaFileName = "";
        for (Map.Entry<Integer, FireRulerClass> entry : annotationMap.entrySet()) {
            FireRulerClass fireRulerClass = entry.getValue();
            javaFileName = javaFileName.length() <= 0 ? fireRulerClass.getPackageName() : javaFileName.length() < fireRulerClass.getPackageName().length() ? javaFileName : fireRulerClass.getPackageName();
            methodBuilder.addJavadoc("$S ==> $S \n", fireRulerClass.getAlias(), fireRulerClass.getClassFullName());
            methodBuilder.addStatement("stoveRuleMap.put($L , $S)", fireRulerClass.getAliasHashCode(), fireRulerClass.getClassFullName());
        }
        printNodeMessage(messager, "Create MethodSpec end!");
        //创建类
        printNodeMessage(messager, "Create Class!");
        TypeSpec typeSpec = TypeSpec.classBuilder(StoveConstant.RouterGenerateFileName + javaFileName.replace(".", ""))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(StoveConstant.RouterInterfacePackageName, StoveConstant.RouterInterfaceName))
                .addMethod(methodBuilder.build())
                .build();
        //输入包名，与Class的参数，返回生成代码的Filer
        JavaFile javaFile = JavaFile.builder(StoveConstant.RouterGenerateFilePackageName, typeSpec).build();

        try {
            printNodeMessage(messager, "write code!");
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            error(messager, null, "javaFile write error @%s", e.toString());
            return false;
        }
        return true;
    }


    //信息输出
    private static void printNodeMessage(Messager messager, String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("[ @%s --NOTE] " + message, RouterRule.class.getSimpleName()));
    }

    //错误信息输出
    private static void error(Messager messager, Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

}
