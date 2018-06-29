package com.x.stoverouter;

import android.app.Activity;
import android.app.Application;

public interface StoveActivityLife extends Application.ActivityLifecycleCallbacks {

    Activity resumedActivity();

    Activity currentActivity();

}
