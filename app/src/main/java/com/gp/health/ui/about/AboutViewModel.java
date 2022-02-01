
package com.gp.health.ui.about;

import com.gp.health.data.DataManager;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;


public class AboutViewModel extends BaseViewModel<AboutNavigator> {

    public AboutViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

}
