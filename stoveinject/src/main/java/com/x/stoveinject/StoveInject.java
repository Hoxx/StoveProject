package com.x.stoveinject;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.x.stoveannotation.StoveConstant;

public class StoveInject {

    public static void bind(Activity activity) {
        bindView(activity);
    }

    public static void bind(Fragment fragment) {
        bindView(fragment);
    }

    private static void bindView(Object host) {

        try {
            Class<?> hostClass = host.getClass();

            String generateClassFullName = StoveConstant.FindIdGenerateFilePackageName +
                    "." +
                    hostClass.getCanonicalName().replace(".", "") +
                    StoveConstant.FindIdGenerateFileName;

            Class<StoveFindIdInterface<Object>> generateClass = (Class<StoveFindIdInterface<Object>>) Class.forName(generateClassFullName);

            StoveFindIdInterface<Object> findIdInterface = generateClass.newInstance();

            if (findIdInterface != null)
                findIdInterface.findViewById(host);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
