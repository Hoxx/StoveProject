package com.x.stoverouter;

import android.util.SparseArray;

public final class StoveRule {

    private static StoveRule instance;

    private SparseArray<String> stoveRuleMap;


    private StoveRule() {
        stoveRuleMap = new SparseArray<>();
    }

    private static StoveRule getInstance() {
        if (instance == null) {
            synchronized (StoveRule.class) {
                if (instance == null) {
                    instance = new StoveRule();
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
            StoveRuleInterface rulerInterface = (StoveRuleInterface) cls.newInstance();
            rulerInterface.addStoveRuleMap(stoveRuleMap);
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
        if (stoveRuleMap.indexOfKey(aliasHashCode) < 0) {
            throw new ClassNotFoundException();
        }
        return Class.forName(stoveRuleMap.get(aliasHashCode));
    }

    private void print() {
        for (int i = 0; i < stoveRuleMap.size(); i++) {
            int key = stoveRuleMap.keyAt(i);
            String value = stoveRuleMap.get(key);
            StoveConstant.Log("print===" + value);
        }
    }
}
