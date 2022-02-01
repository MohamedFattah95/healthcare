
package com.gp.health.di.component;

import android.app.Application;

import com.gp.health.BaseApp;
import com.gp.health.data.DataManager;
import com.gp.health.di.module.AppModule;
import com.gp.health.utils.rx.SchedulerProvider;

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
