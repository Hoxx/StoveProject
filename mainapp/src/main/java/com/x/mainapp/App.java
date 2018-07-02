package com.x.mainapp;

import com.x.stove.StoveApplication;
import com.x.stoverouter.StoveRouter;


public class App extends StoveApplication {

    @Override
    public void initialize() {
        StoveRouter.initialize(this, true);
    }

}
