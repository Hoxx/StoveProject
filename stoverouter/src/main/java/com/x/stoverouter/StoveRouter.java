package com.x.stoverouter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.SparseArray;

import java.util.Set;

public class StoveRouter {

    //单例
    private static StoveRouter    instance;
    //PackageManager
    private        PackageManager packageManager;

    //以获取的Activity的缓存
    private SparseArray<Class<?>> routerActivityCache;

    private StoveRouter() {
        routerActivityCache = new SparseArray<>();
    }

    //获取单例
    private static StoveRouter getInstance() {
        if (instance == null) {
            synchronized (StoveRouter.class) {
                if (instance == null) {
                    instance = new StoveRouter();
                }
            }
        }
        return instance;
    }

    //初始化
    public static void initialize(Application app, boolean debug) {
        Log.e("TAG", "StoveRouter initialize-----");
        if (app == null)
            throw new NullPointerException("StoveRouter register not be null params");
        StoveConstant.DEBUG = debug;
        getInstance().init(app);
    }

    //跳转Activity
    public static void startActivity(Activity activity, String alias) {
        getInstance().startActivityIntent(activity, alias, null);
    }

    //跳转Activity
    public static void startActivity(Activity activity, String alias, onPutExtra onPutExtra) {
        getInstance().startActivityIntent(activity, alias, onPutExtra);
    }

    //创建初始化的参数
    private void init(Application app) {
        packageManager = app.getPackageManager();
        registerRulerMap(app.getPackageName());
    }

    //注册Activity的路由表
    private void registerRulerMap(String currentPkgName) {
        Set<String> pkgNames = StoveRouterUtil.getAppAllActivityList(packageManager, currentPkgName);
        for (String pkgName : pkgNames) {
            String className = StoveConstant.FIRE_RULER_INSTANCE_PACKAGE_NAME + "." + StoveConstant.FIRE_RULER_INSTANCE_CLASS_NAME + pkgName.replace(".", "");
            StoveRule.registerRulerMap(className);
        }
    }

    //启动Activity
    private void startActivityIntent(Activity activity, String alias, onPutExtra onPutExtra) {
        //获取匹配的结果
        Class<?> cls = getClassForActivity(alias);
        if (cls == null) {
            StoveConstant.Log("StoveRouter search Activity instance is empty");
            return;
        }
        if (activityLife.currentActivity()==null){
            StoveConstant.Log("StoveRouter current Activity instance is empty");
            return;
        }
        //创建跳转
        Intent intent = new Intent(activity, cls);
        //检查当前Activity是否存在
        if (intent.resolveActivity(packageManager) == null) {
            StoveConstant.Log(cls.getCanonicalName() + " Not exist!");
            return;
        }
        //设置传参
        if (onPutExtra != null) {
            onPutExtra.onExtra(alias, intent);
        }
        //跳转
        activity.startActivity(intent);
    }

    //获取Class
    private Class<?> getClassForActivity(String alias) {
        //获取匹配规则
        try {
            int hashCode = alias.hashCode();
            if (routerActivityCache.indexOfKey(hashCode) >= 0) {
                return routerActivityCache.get(hashCode);
            }
            return StoveRule.getActivityClass(hashCode);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
