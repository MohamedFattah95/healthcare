
package com.gp.health.ui.user.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.HashMap;

public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    private MutableLiveData<DataWrapperModel<UserModel>> loginLiveData;

    public LoginViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        loginLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<UserModel>> getLoginLiveData() {
        return loginLiveData;
    }

    public void doLogin(String mobile, String password) {

        getCompositeDisposable().add(getDataManager()
                .doLoginApiCall(mobile, password)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        getDataManager().setUserObject(response.getData());
                        getDataManager().updateUserInfo(response.getData(), DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                        getDataManager().setAccessToken(response.getData().getAccessToken());
                        loginLiveData.setValue(response);
                    } else if (response.getCode() == 4011) {
                        getNavigator().openVerificationActivity(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void updateFCMToken(int userId, HashMap<String, String> map) {

        getCompositeDisposable().add(getDataManager()
                .updateFCMTokenApiCall(userId, map)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe());
    }

    public void getSettings() {
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
}
