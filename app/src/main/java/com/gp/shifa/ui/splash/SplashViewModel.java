
package com.gp.shifa.ui.splash;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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
