
package com.gp.health.ui.main;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;


public class MainViewModel extends BaseViewModel<MainNavigator> {

    private MutableLiveData<DataWrapperModel<Void>> logoutLiveData;

    public MainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        logoutLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<Void>> getLogoutLiveData() {
        return logoutLiveData;
    }


    public void getAppSettings() {
        getCompositeDisposable().add(getDataManager()
                .getSettings()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        getDataManager().setSettingsObject(response.getData());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void doLogout() {
        getCompositeDisposable().add(getDataManager()
                .doLogout(getDataManager().getCurrentUserId())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response.getCode() == 200) {
                        logoutLiveData.setValue(response);
                    } else
                        getNavigator().showMyApiMessage(response.getMessage());
                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
