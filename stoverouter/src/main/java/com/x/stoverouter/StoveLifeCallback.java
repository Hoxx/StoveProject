package com.x.stoverouter;

import android.app.Activity;
import android.os.Bundle;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class StoveLifeCallback implements StoveActivityLife {

    private WeakReference<Activity> weakReference;

    StoveLifeCallback() {
    }


    @Override
    public void onActivityResumed(Activity activity) {
        weakReference = new WeakReference<>(activity);
    }


    @Override
    public Activity resumedActivity() {
        return weakReference.get();
    }

    @Override
    public Activity currentActivity() {
        return resumedActivity();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
