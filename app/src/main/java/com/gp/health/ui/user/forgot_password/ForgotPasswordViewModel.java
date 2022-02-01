
package com.gp.health.ui.user.forgot_password;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class ForgotPasswordViewModel extends BaseViewModel<ForgotPasswordNavigator> {
    private MutableLiveData<DataWrapperModel<UserModel>> forgotPasswordLiveData;

    public ForgotPasswordViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        forgotPasswordLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<UserModel>> getForgotPasswordCodeLiveData() {
        return forgotPasswordLiveData;
    }

    public void sendForgotPasswordCode(String fullNumber) {
        getCompositeDisposable().add(getDataManager()
                .sendForgotPasswordCodeApiCall(fullNumber)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200 || response.getCode() == 4012)
                        forgotPasswordLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
