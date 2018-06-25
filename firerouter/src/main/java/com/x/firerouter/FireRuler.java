package com.x.firerouter;

import android.util.SparseArray;

public class FireRuler {

    private static FireRulerInterface fireRule;
    private static FireRuler instance;

    private SparseArray<Class<?>> fireRuleMap;


    FireRuler() {
        fireRuleMap = new SparseArray<>();
    }

    private static FireRuler getInstance() {
        if (instance == null) {
            synchronized (FireRuler.class) {
                if (instance == null) {
                    instance = new FireRuler();
                }
            }
        }
        return instance;
    }

    //获取单例
    public static FireRulerInterface getFireRule() {
        if (fireRule == null) {
            synchronized (FireRuler.class) {
                if (fireRule == null) {
                    fireRule = getFireRulerInstance(FireConstant.FIRE_RULER_INSTANCE_PACKAGE_NAME, FireConstant.FIRE_RULER_INSTANCE_CLASS_NAME);
                }
            }
        }
        return fireRule;
    }

    //通过类名创建实例
    private static FireRulerInterface getFireRulerInstance(String pkgName, String clsName) {
        try {
            Class<?> cls = Class.forName(pkgName + "." + clsName);
            return (FireRulerInterface) cls.newInstance();
        } catch (ClassNotFoundException e) {
            FireConstant.Log("【" + pkgName + "." + clsName + "】Not exist!");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            FireConstant.Log("【" + pkgName + "." + clsName + "】" + e.toString());
        } catch (InstantiationException e) {
            e.printStackTrace();
            FireConstant.Log("【" + pkgName + "." + clsName + "】" + e.toString());
        }
        return null;
    }

    public static void addRulerMap(int aliasHashCode, Class<?> cls) {
        getInstance().addMap(aliasHashCode, cls);
    }

    public static Class<?> getAliasClass(int aliasHashCode) {
        return getInstance().getAlias(aliasHashCode);
    }


    private void addMap(int aliasHashCode, Class<?> cls) {
        Class<?> clz = fireRuleMap.get(aliasHashCode);
        if (clz == null) {
            fireRuleMap.put(aliasHashCode, cls);
        }
    }

    private Class<?> getAlias(int aliasHashCode) {
        return fireRuleMap.get(aliasHashCode);
    }
}
