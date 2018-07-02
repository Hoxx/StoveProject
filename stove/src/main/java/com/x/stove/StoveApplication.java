package com.x.stove;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public abstract class StoveApplication extends Application {

    public RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary();
        initialize();
    }

    //内存泄露检测
    private void initLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(getApplicationContext())) {
//            return;
//        }
        refWatcher = LeakCanary.install(this);
    }

    //检测其他内存泄露（Fragment,Service,BroadCastReceiver）
    public static RefWatcher getRefWatcher(Context context) {
        StoveApplication stoveApplication = (StoveApplication) context.getApplicationContext();
        return stoveApplication.refWatcher;
    }

    public abstract void initialize();
}
