package com.x.firerouter;

import android.util.SparseArray;

public final class FireRuler {

    private static FireRuler instance;

    private SparseArray<String> fireRuleMap;


    private FireRuler() {
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

    //注册Activity
    public static void registerRulerMap(String className) {
        getInstance().callInterfaceMethod(className);
    }

    //获取Activity
    public static Class<?> getActivityClass(int aliasHashCode) throws ClassNotFoundException {
        return getInstance().getAlias(aliasHashCode);
    }

    //调用生成类的方法，注册Activity
    //注释掉"e.printStackTrace();"，防止为空类名的崩溃
    private void callInterfaceMethod(String className) {
        try {
            Class<?> cls = Class.forName(className);
            FireRulerInterface rulerInterface = (FireRulerInterface) cls.newInstance();
            rulerInterface.addFireRulerMap(fireRuleMap);
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
        } catch (InstantiationException e) {
//            e.printStackTrace();
        }
    }

    //根据别名的Hash值，获取对应Activity
    private Class<?> getAlias(int aliasHashCode) throws ClassNotFoundException {
        print();
        if (fireRuleMap.indexOfKey(aliasHashCode) < 0) {
            throw new ClassNotFoundException();
        }
        return Class.forName(fireRuleMap.get(aliasHashCode));
    }

    private void print() {
        for (int i = 0; i < fireRuleMap.size(); i++) {
            int key = fireRuleMap.keyAt(i);
            String value = fireRuleMap.get(key);
            FireConstant.Log("print===" + value);
        }
    }
}
