

package com.gp.shifa.ui.settings;


import com.gp.shifa.data.DataManager;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

public class SettingsViewModel extends BaseViewModel<SettingsNavigator> {

    public SettingsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

}
