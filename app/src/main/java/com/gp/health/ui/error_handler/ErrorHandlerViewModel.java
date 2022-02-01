
package com.gp.health.ui.error_handler;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class ErrorHandlerViewModel extends BaseViewModel<ErrorHandlerNavigator> {

    private MutableLiveData<DataWrapperModel<Void>> checkUserLiveData;

    public ErrorHandlerViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        checkUserLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<Void>> getCheckUserLiveData() {
        return checkUserLiveData;
    }

    public void checkUser(int currentUserId) {
        getCompositeDisposable().add(getDataManager()
                .checkUserApiCall(currentUserId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        checkUserLiveData.setValue(response);
                    else if (response.getCode() == 401)
                        getNavigator().showUserDeletedMsg();
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
