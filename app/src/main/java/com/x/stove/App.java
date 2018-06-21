package com.x.stove;

import android.app.Application;

import com.x.firerouter.FireRouter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FireRouter.initialize(this, true);
    }
}
