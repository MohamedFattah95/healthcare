
package com.gp.shifa.ui.main;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;


public class MainViewModel extends BaseViewModel<MainNavigator> {

    private MutableLiveData<DataWrapperModel<Void>> logoutLiveData;

    public MainViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        logoutLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<Void>> getLogoutLiveData() {
        return logoutLiveData;
    }

    public void doLogout() {
        getCompositeDisposable().add(getDataManager()
                .doLogout()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response.getStatus().equals("1")) {
                        logoutLiveData.setValue(response);
                    } else {
                        getDataManager().setUserAsLoggedOut();
                        getNavigator().openLoginActivity();
                    }
                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
