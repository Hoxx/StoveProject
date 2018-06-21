package com.x.firerouter;

public class FireRuler {

    private static FireRulerInterface fireRule;

    FireRuler() {

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
}
