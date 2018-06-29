package com.x.stoveinject;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

public class StoveInject {

    public static void bind(Activity activity) {
        bind(activity, activity);
    }

    public static void bind(Fragment fragment) {
        bind(fragment, fragment.getView());
    }


    private static void bind(Object host, Object object) {

        try {
            Class<?> aClass = host.getClass();

            String proxyClassFullName = aClass.getName() + "$$StoveInject";

            Log.e("TAG", "proxyClassFullName:" + proxyClassFullName);
            Class<?> proxyClass = Class.forName(proxyClassFullName);

            StoveFindIdInterface findIdInterface = (StoveFindIdInterface) proxyClass.newInstance();
            Log.e("TAG", "xapInterface != null:" + (findIdInterface != null));
            if (findIdInterface != null)
                findIdInterface.findViewById(host, object);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
