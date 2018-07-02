package com.x.stoveprocessor.findid;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.x.stoveannotation.StoveConstant;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;

/*对应一个类内部的所有注解*/
public class StoveFindIdGroup {

    private Map<Integer, StoveFindIdView> findIdViewMap = new HashMap<>();
    private TypeName classTypeName;

    public StoveFindIdGroup(TypeName classTypeName) {
        this.classTypeName = classTypeName;
    }

    //收集一个类中的所有该注解的变量信息
    public void addFindIdView(int id, StoveFindIdView view) {
        if (!findIdViewMap.containsKey(id)) {
            findIdViewMap.put(id, view);
        }
    }

    //生成代码
    public JavaFile generateJavaCode() {
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("findViewById")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addParameter(classTypeName, "host");

        //创建方法
        /* $T:代表的是一个类型，不能用String替换,$N:代表的是Name $S:代表的是字符串资源 $L:代表的是long型 */

        for (Map.Entry<Integer, StoveFindIdView> entry : findIdViewMap.entrySet()) {
            if (entry.getValue().isFragment()) {
                methodSpec.addStatement("host.$N = ($T)host.getView().findViewById($L)",
                        entry.getValue().getViewName(),
                        entry.getValue().getViewTypeName(),
                        entry.getKey());
            } else {
                methodSpec.addStatement("host.$N = ($T)host.findViewById($L)",
                        entry.getValue().getViewName(),
                        entry.getValue().getViewTypeName(),
                        entry.getKey());
            }
        }
        TypeSpec typeSpec = TypeSpec.classBuilder(classTypeName.toString().replace(".", "") + StoveConstant.FindIdGenerateFileName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(StoveConstant.FindIdInterfacePackageName, StoveConstant.FindIdInterfaceName), classTypeName))
                .addMethod(methodSpec.build())
                .build();
        return JavaFile.builder(StoveConstant.FindIdGenerateFilePackageName, typeSpec).build();
    }

}
