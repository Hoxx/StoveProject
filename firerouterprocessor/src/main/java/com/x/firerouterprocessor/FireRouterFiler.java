package com.x.firerouterprocessor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.x.firerouterannotation.FireRule;

import java.io.IOException;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;

public class FireRouterFiler {

    private final static String classFileName = "$$FireRuler";
    private final static String packageName = "com.x.firerouter";
    private final static String interfaceName = "FireRulerInterface";

    private final static String generateFilePackageName = "com.x.firerouter.generate";

    /* 生成代码:样式
    * public class $$FireRuler implements FireRulerInterface {
        @Override
        public Class getAlias(String alias) throws ClassNotFoundException {
            switch (alias.hashCode()) {
                case 1136912392: //"MainActivity=com.x.mainapp"
                    return Class.forName("com.x.mainapp.MainActivity") ;
                case 1812103677: //"InfoActivity=com.x.mainapp.info"
                    return Class.forName("com.x.mainapp.info.InfoActivity") ;
            }
           return null;
        }
      }
    */
    public static boolean generateJavaCode(Filer filer, Messager messager, Map<Integer, FireRulerClass> annotationMap) {
        if (annotationMap.isEmpty()) return false;
        printNodeMessage(messager, "Create MethodSpec!");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("getAlias")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(Class.class)
                .addParameter(TypeName.get(String.class), "alias")
                .addException(TypeName.get(ClassNotFoundException.class));
        printNodeMessage(messager, "Create CodeBlock!");
        methodBuilder.beginControlFlow("switch (alias.hashCode())");

        for (Map.Entry<Integer, FireRulerClass> entry : annotationMap.entrySet()) {
            FireRulerClass fireRulerClass = entry.getValue();
            methodBuilder.addStatement("case $L: //$S \n $> return Class.forName($S) $<", fireRulerClass.getAliasHashCode(), fireRulerClass.getAlias() + "=" + fireRulerClass.getPackageName(), fireRulerClass.getClassFullName());
        }
        methodBuilder.endControlFlow();
        methodBuilder.addStatement("return null");
        printNodeMessage(messager, "Create MethodSpec end!");
        //创建类
        printNodeMessage(messager, "Create Class!");
        TypeSpec typeSpec = TypeSpec.classBuilder(classFileName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(packageName, interfaceName))
                .addMethod(methodBuilder.build())
                .build();
        //输入包名，与Class的参数，返回生成代码的Filer
        JavaFile javaFile = JavaFile.builder(generateFilePackageName, typeSpec).build();

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

    /* 生成代码:样式
    * public class $$FireRulerPackageName implements FireRulerInterface {

        @Override
        public void addFireRulerMap(Map<Integer,String> fireRulerMap) {
            fireRulerMap.put(1136912392,"com.x.mainapp.MainActivity");
            fireRulerMap.put(1812103677,"com.x.mainapp.info.InfoActivity");
        }

      }
    */
    public static boolean generateCode(Filer filer, Messager messager, Map<Integer, FireRulerClass> annotationMap) {
        if (annotationMap.isEmpty()) return false;

        printNodeMessage(messager, "Create Override Method Param!");
        //Override Method Param
        ClassName className = ClassName.get("android.util", "SparseArray");
        TypeName paramType = ParameterizedTypeName.get(className, TypeName.get(String.class));
        ParameterSpec parameterSpec = ParameterSpec.builder(paramType, "fireRulerMap").build();
        printNodeMessage(messager, "Create Method!");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("addFireRulerMap")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement("$T.out.println($S)", System.class, "******************************************************************************")
                .addParameter(parameterSpec);
        //generate File Name
        String javaFileName = "";
        for (Map.Entry<Integer, FireRulerClass> entry : annotationMap.entrySet()) {
            FireRulerClass fireRulerClass = entry.getValue();
            javaFileName = javaFileName.length() <= 0 ? fireRulerClass.getPackageName() : javaFileName.length() < fireRulerClass.getPackageName().length() ? javaFileName : fireRulerClass.getPackageName();
            methodBuilder.addJavadoc("$S ==> $S \n", fireRulerClass.getAlias(), fireRulerClass.getClassFullName());
            methodBuilder.addStatement("fireRulerMap.put($L , $S)", fireRulerClass.getAliasHashCode(), fireRulerClass.getClassFullName());
        }
        printNodeMessage(messager, "Create MethodSpec end!");
        //创建类
        printNodeMessage(messager, "Create Class!");
        TypeSpec typeSpec = TypeSpec.classBuilder(classFileName + javaFileName.replace(".", ""))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(packageName, interfaceName))
                .addMethod(methodBuilder.build())
                .build();
        //输入包名，与Class的参数，返回生成代码的Filer
        JavaFile javaFile = JavaFile.builder(generateFilePackageName, typeSpec).build();

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

    /* 生成代码:样式
          public class xxx$$FireRuler {

              static {
                  FireRuler.addRulerMap(1136912392,"com.x.mainapp.MainActivity");
                  FireRuler.addRulerMap(1812103677,"com.x.mainapp.info.InfoActivity");
              }

          }
    */
    public static boolean generateStaticCode(Filer filer, Messager messager, Map<Integer, FireRulerClass> annotationMap) {
        if (annotationMap.isEmpty()) return false;
        printNodeMessage(messager, "Create static CodeBlock!");

        CodeBlock.Builder staticCodeBlockBuilder = CodeBlock.builder()
                .addStatement("$T.out.println($S)", System.class, classFileName + annotationMap.entrySet().iterator().next().getValue().getPackageName().replace(".", ""));
        //静态方法的类
        ClassName FireRulerClass = ClassName.get(packageName, "FireRuler");

        String javaFileName = "";
        for (Map.Entry<Integer, FireRulerClass> entry : annotationMap.entrySet()) {
            FireRulerClass fireRulerClass = entry.getValue();
            javaFileName = javaFileName.length() <= 0 ? fireRulerClass.getPackageName() : javaFileName.length() < fireRulerClass.getPackageName().length() ? javaFileName : fireRulerClass.getPackageName();
            staticCodeBlockBuilder.addStatement("$T.addRulerMap($L,$S) ", FireRulerClass, fireRulerClass.getAliasHashCode(), fireRulerClass.getClassFullName());
        }
        printNodeMessage(messager, "Create static Code end!");
        //创建类
        printNodeMessage(messager, "Create Class!");
        TypeSpec typeSpec = TypeSpec.classBuilder(classFileName + javaFileName.replace(".", ""))
                .addModifiers(Modifier.PUBLIC)
                .addStaticBlock(staticCodeBlockBuilder.build())
                .build();
        //输入包名，与Class的参数，返回生成代码的Filer
        JavaFile javaFile = JavaFile.builder(generateFilePackageName, typeSpec).build();

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

    /* 生成代码:样式
          public class xxx$$FireRuler {

              private static boolean INIT = init();

              public static boolean init() {
                  FireRuler.addRulerMap(1136912392,"com.x.mainapp.MainActivity");
                  FireRuler.addRulerMap(1812103677,"com.x.mainapp.info.InfoActivity");
                  return true;
              }
          }
    */
    public static boolean generateStaticJavaCode(Filer filer, Messager messager, Map<Integer, FireRulerClass> annotationMap) {
        if (annotationMap.isEmpty()) return false;
        printNodeMessage(messager, "Create Field!");

        FieldSpec staticField = FieldSpec.builder(Boolean.class, "INIT", Modifier.PRIVATE, Modifier.STATIC)
                .initializer("init()").build();

        MethodSpec.Builder staticMethodBuilder = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.STATIC)
                .addStatement("$T.out.println($S)", System.class, classFileName + annotationMap.entrySet().iterator().next().getValue().getPackageName().replace(".", ""))
                .returns(Boolean.class);

        //静态方法的类
        ClassName FireRulerClass = ClassName.get(packageName, "FireRuler");

        String javaFileName = "";
        for (Map.Entry<Integer, FireRulerClass> entry : annotationMap.entrySet()) {
            FireRulerClass fireRulerClass = entry.getValue();
            javaFileName = javaFileName.length() <= 0 ? fireRulerClass.getPackageName() : javaFileName.length() < fireRulerClass.getPackageName().length() ? javaFileName : fireRulerClass.getPackageName();
//            staticMethodBuilder.addStatement("//$S \n  $T.addRulerMap($L,$S) ", fireRulerClass.getAlias() + "=" + fireRulerClass.getPackageName(), FireRulerClass, fireRulerClass.getAliasHashCode(), fireRulerClass.getClassFullName());
//            staticMethodBuilder.addStatement("$T.addRulerMap($L,$S) ", FireRulerClass, fireRulerClass.getAliasHashCode(), fireRulerClass.getClassFullName());
        }
        staticMethodBuilder.addStatement("return true");
        printNodeMessage(messager, "Create MethodSpec end!");
        //创建类
        printNodeMessage(messager, "Create Class!");
        TypeSpec typeSpec = TypeSpec.classBuilder(classFileName + javaFileName.replace(".", ""))
                .addModifiers(Modifier.PUBLIC)
                .addField(staticField)
                .addMethod(staticMethodBuilder.build())
                .build();
        //输入包名，与Class的参数，返回生成代码的Filer
        JavaFile javaFile = JavaFile.builder(generateFilePackageName, typeSpec).build();

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
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("[ @%s --NOTE] " + message, FireRule.class.getSimpleName()));
    }

    //错误信息输出
    private static void error(Messager messager, Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

}
