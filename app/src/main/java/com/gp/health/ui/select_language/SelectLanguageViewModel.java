
package com.gp.health.ui.select_language;

import com.gp.health.data.DataManager;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class SelectLanguageViewModel extends BaseViewModel<SelectLanguageNavigator> {

    public SelectLanguageViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }


}
