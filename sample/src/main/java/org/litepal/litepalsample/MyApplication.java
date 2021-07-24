package org.litepal.litepalsample;

import android.app.Application;

import org.litepal.copy.LitePalCopy;

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        LitePalCopy.initialize(this);
    }
}
