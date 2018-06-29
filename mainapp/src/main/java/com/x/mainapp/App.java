package com.x.mainapp;

import android.app.Application;

import com.x.stoverouter.StoveRouter;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StoveRouter.initialize(this, true);
    }

}
