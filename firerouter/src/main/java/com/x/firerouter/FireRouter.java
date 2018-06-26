package com.x.firerouter;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Set;

public class FireRouter {

    //单例
    private static FireRouter instance;
    //PackageManager
    private PackageManager packageManager;

    //用于获取App当前的onResume的Activity
    private FireActivityLife activityLife;

    private FireRouter() {
        activityLife = new FireLifeCallback();
    }

    //获取单例
    private static FireRouter getInstance() {
        if (instance == null) {
            synchronized (FireRouter.class) {
                if (instance == null) {
                    instance = new FireRouter();
                }
            }
        }
        return instance;
    }

    //初始化
    public static void initialize(Application app, boolean debug) {
        Log.e("TAG", "FireRouter initialize-----");
        if (app == null)
            throw new NullPointerException("FireRouter register not be null params");
        FireConstant.DEBUG = debug;
        getInstance().init(app);
    }

    //跳转Activity
    public static void startActivity(String alias) {
        getInstance().startActivityIntent(alias, null);
    }

    //跳转Activity
    public static void startActivity(String alias, onPutExtra onPutExtra) {
        getInstance().startActivityIntent(alias, onPutExtra);
    }

    //创建初始化的参数
    private void init(Application app) {
        packageManager = app.getPackageManager();
        registerRulerMap(app.getPackageName());
        registerActivityLife(app);
    }

    //注册Activity的路由表
    private void registerRulerMap(String currentPkgName) {
        Set<String> pkgNames = FireRouterUtil.getAppAllActivityList(packageManager, currentPkgName);
        for (String pkgName : pkgNames) {
            String className = FireConstant.FIRE_RULER_INSTANCE_PACKAGE_NAME + "." + FireConstant.FIRE_RULER_INSTANCE_CLASS_NAME + pkgName.replace(".", "");
            FireRuler.registerRulerMap(className);
        }
    }

    //注册Activity的生命周期回调，用于获取当前Activity实例，启动跳转
    private void registerActivityLife(Application app) {
        if (activityLife == null)
            activityLife = new FireLifeCallback();
        app.registerActivityLifecycleCallbacks(activityLife);
    }

    //启动Activity
    private void startActivityIntent(String alias, onPutExtra onPutExtra) {
        //获取匹配的结果
        Class<?> cls = getClassForActivity(alias);
        if (cls == null) {
            FireConstant.Log("FireRouter search Activity instance empty");
            return;
        }
        //创建跳转
        Intent intent = new Intent(activityLife.currentActivity(), cls);
        //检查当前Activity是否存在
        if (intent.resolveActivity(packageManager) == null) {
            FireConstant.Log(cls.getCanonicalName() + " Not exist!");
            return;
        }
        //设置传参
        if (onPutExtra != null) {
            onPutExtra.onExtra(alias, intent);
        }
        //跳转
        activityLife.currentActivity().startActivity(intent);
    }

    //获取Class
    private Class<?> getClassForActivity(String alias) {
        //获取匹配规则
        try {
            return FireRuler.getActivityClass(alias.hashCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
