package com.example.cardview_test.aplication;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

public class CardApplication extends Application {
    public static Context mApplicationContext;

    public static final String APPLICATION_ID = "05eb25bc0ce45d0a6ae5e194712907ca";

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        Bmob.initialize(this,APPLICATION_ID);
    }
}
