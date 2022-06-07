
package com.gp.shifa;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.gp.shifa.di.component.AppComponent;
import com.gp.shifa.di.component.DaggerAppComponent;
import com.gp.shifa.utils.AppLogger;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BaseApp extends Application {

    public AppComponent appComponent;

    @Inject
    CalligraphyConfig mCalligraphyConfig;

    @SuppressLint("StaticFieldLeak")
    public static Context context;
    @SuppressLint("StaticFieldLeak")
    private static BaseApp instance;


    public static Context getContext() {
        return context;
    }

    public static BaseApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if(instance == null){
            instance = this;
        }

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();

        appComponent.inject(this);

        AppLogger.init();

        CalligraphyConfig.initDefault(mCalligraphyConfig);
    }
}
