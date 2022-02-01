

package com.gp.health.ui.settings;


import com.gp.health.data.DataManager;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class SettingsViewModel extends BaseViewModel<SettingsNavigator> {

    public SettingsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

}
