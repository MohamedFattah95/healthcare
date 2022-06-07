
package com.gp.shifa.ui.select_language;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

public class SelectLanguageViewModel extends BaseViewModel<SelectLanguageNavigator> {

    public SelectLanguageViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }


}
