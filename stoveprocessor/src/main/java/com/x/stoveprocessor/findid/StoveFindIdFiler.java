package com.x.stoveprocessor.findid;

import com.x.stoveannotation.FindId;

import java.io.IOException;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class StoveFindIdFiler {

    public static boolean generateJavaCode(Map<String, StoveFindIdGroup> findIdGroupMap, Filer filer, Messager messager) {
        try {
            for (Map.Entry<String, StoveFindIdGroup> entry : findIdGroupMap.entrySet()) {
                entry.getValue().generateJavaCode().writeTo(filer);
            }
            return true;
        } catch (IOException e) {
            printErrorMessage(messager, e.toString());
            return false;
        }
    }


    //输出信息
    private static void printErrorMessage(Messager messager, String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format("[ @%s --ERROR] " + message, FindId.class.getSimpleName()));
    }
}
