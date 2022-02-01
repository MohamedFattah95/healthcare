
package com.gp.health.ui.splash;

import com.gp.health.data.DataManager;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class SplashViewModel extends BaseViewModel<SplashNavigator> {

    public SplashViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void decideNextActivity() {

        if (getDataManager().isFirstTimeLaunch()) {
            getNavigator().openLanguageActivity();
            getDataManager().setFirstTimeLaunch(false);
        } else {
            getNavigator().openMainActivity();
        }


    }
}
