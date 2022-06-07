
package com.gp.shifa.di.component;

import android.app.Application;

import com.gp.shifa.BaseApp;
import com.gp.shifa.data.DataManager;
import com.gp.shifa.di.module.AppModule;
import com.gp.shifa.utils.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;


@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(BaseApp app);

    DataManager getDataManager();

    SchedulerProvider getSchedulerProvider();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
