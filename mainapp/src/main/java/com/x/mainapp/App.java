package com.x.mainapp;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.x.firerouter.FireRouter;
import com.x.firerouter.FireRouterUtil;

import java.util.Set;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FireRouter.initialize(this, true);

//        PackageManager manager = getPackageManager();
//
//        String packageName = getPackageName();
//        Set<String> set = FireRouterUtil.getAppAllActivityList(getPackageManager(), packageName);
//
//        for (String name : set) { Log.e("TAG", name);
//        }
    }

}
