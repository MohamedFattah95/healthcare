

package com.gp.health.ui.user.verify_account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.HashMap;

public class VerifyAccountViewModel extends BaseViewModel<VerifyAccountNavigator> {

    private MutableLiveData<DataWrapperModel<Void>> verifyCodeLiveData;
    private MutableLiveData<DataWrapperModel<Integer>> resendCodeLiveData;

    public VerifyAccountViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        verifyCodeLiveData = new MutableLiveData<>();
        resendCodeLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<Void>> getVerifyCodeLiveData() {
        return verifyCodeLiveData;
    }

    public LiveData<DataWrapperModel<Integer>> getResendCodeLiveData() {
        return resendCodeLiveData;
    }

    public void verifyCode(UserModel userModel, String code) {

        getCompositeDisposable().add(getDataManager()
                .verifyCodeApiCall(String.valueOf(userModel.getId()), code)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {

                        getDataManager().setUserObject(userModel);
                        getDataManager().updateUserInfo(userModel, DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);

                        verifyCodeLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void resendCode(int userId) {
        getCompositeDisposable().add(getDataManager()
                .resendCodeApiCall(userId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        resendCodeLiveData.setValue(response);
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
