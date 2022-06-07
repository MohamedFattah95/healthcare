
package com.gp.shifa.ui.error_handler;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

public class ErrorHandlerViewModel extends BaseViewModel<ErrorHandlerNavigator> {

    private MutableLiveData<DataWrapperModel<Void>> checkUserLiveData;

    public ErrorHandlerViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        checkUserLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<Void>> getCheckUserLiveData() {
        return checkUserLiveData;
    }

}
