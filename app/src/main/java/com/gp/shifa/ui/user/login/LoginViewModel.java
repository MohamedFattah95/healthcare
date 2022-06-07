
package com.gp.shifa.ui.user.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

    public void doLogin(String email, String password) {

        getCompositeDisposable().add(getDataManager()
                .doLoginApiCall(email, password)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1")) {
                        getDataManager().setUserObject(response.getData());
                        getDataManager().updateUserInfo(response.getData(), DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                        getDataManager().setAccessToken(response.getData().getAccessToken());
                        loginLiveData.setValue(response);
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

}
