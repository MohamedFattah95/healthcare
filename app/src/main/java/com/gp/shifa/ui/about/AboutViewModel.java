
package com.gp.shifa.ui.about;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;


public class AboutViewModel extends BaseViewModel<AboutNavigator> {

    public AboutViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

}
