
package com.gp.health.ui.media;

import com.gp.health.data.DataManager;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;


public class MediaViewModel extends BaseViewModel<MediaNavigator> {

    public MediaViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

}
