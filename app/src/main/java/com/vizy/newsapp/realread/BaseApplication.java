package com.vizy.newsapp.realread;

import android.app.Application;
//import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccountKit;
import com.facebook.appevents.AppEventsLogger;

public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //MultiDex.install(this);
        AccountKit.initialize(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
