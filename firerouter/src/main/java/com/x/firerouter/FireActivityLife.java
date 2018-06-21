package com.x.firerouter;

import android.app.Activity;
import android.app.Application;

public interface FireActivityLife extends Application.ActivityLifecycleCallbacks {

    Activity resumedActivity();

    Activity currentActivity();

}
