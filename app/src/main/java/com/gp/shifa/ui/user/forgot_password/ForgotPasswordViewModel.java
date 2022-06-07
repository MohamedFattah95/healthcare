
package com.gp.shifa.ui.user.forgot_password;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1"))
                        forgotPasswordLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
