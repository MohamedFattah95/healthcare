
package com.gp.health.ui.user.reset_password;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class ResetPasswordViewModel extends BaseViewModel<ResetPasswordNavigator> {
    private MutableLiveData<DataWrapperModel<Void>> resetPasswordLiveData;

    public ResetPasswordViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        resetPasswordLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<Void>> getResetPasswordLiveData() {
        return resetPasswordLiveData;
    }

    public void resetPassword(String code, String phone, String password) {
        getCompositeDisposable().add(getDataManager()
                .resetPasswordApiCall(code, phone, password)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        resetPasswordLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
