package com.x.stove;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Author:         Hxx
 * Description:    说明：
 * <p>
 * density 在每个设备上都是固定的，DPI / 160 = density，
 * 屏幕的总 px 宽度 / density = 屏幕的总 dp 宽度==>屏幕的总 px 宽度 / 屏幕的总 dp 宽度 = density
 * 适配时需要让设计图宽度的DP值与屏幕宽度的DP值相等，便可适配屏幕，所以：
 * --------屏幕的总 px 宽度 / 设计图的总 dp 宽度 = density--------
 * 只需要动态调整density的值就可以
 * <p>
 * ProjectName:    StoveProject
 * Package:        com.x.stove
 * CreateDate:     2018/9/15 0:29
 */
public class ScreenAdaptation implements Application.ActivityLifecycleCallbacks {

    private static ScreenAdaptation instance = new ScreenAdaptation();

    private int UIWidthDP;
    private DisplayMetrics appDisplayMetrics;
    private float appDensity;
    private float appScaledDensity;

    private static ScreenAdaptation getInstance() {
        return instance;
    }

    /**
     * 适配
     */
    public static void init(Application application, int UIWidthDP) {
        getInstance().initAdaptation(application, UIWidthDP);
    }

    /**
     * 初始化适配方法
     *
     * @param application 程序的Application
     * @param UIWidthDP   UI设计图的宽度DP值
     */
    private void initAdaptation(Application application, int UIWidthDP) {
        if (application != null) {
            application.registerActivityLifecycleCallbacks(this);
            appDisplayMetrics = application.getResources().getDisplayMetrics();
            appDensity = appDisplayMetrics.density;
            appScaledDensity = appDisplayMetrics.scaledDensity;
        }
        this.UIWidthDP = UIWidthDP;

    }

    /**
     * 重新计算当前Activity的Density值
     *
     * @param activity 当前需要适配的Activity
     */
    private void adaptation(Activity activity) {
        //计算新的density
        float newDensity = ((float) appDisplayMetrics.widthPixels) / ((float) UIWidthDP);
        //计算新的ScaledDensity
        float newScaledDensity = newDensity * (appScaledDensity / appDensity);
        //计算新的DensityDpi
        int newDensityDpi = (int) (160 * newDensity);


        //修改Activity的DensityMetrics的值
        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = newDensity;
        activityDisplayMetrics.scaledDensity = newScaledDensity;
        activityDisplayMetrics.densityDpi = newDensityDpi;


    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        adaptation(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
