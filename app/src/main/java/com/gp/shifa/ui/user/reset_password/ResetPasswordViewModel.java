
package com.gp.shifa.ui.user.reset_password;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1"))
                        resetPasswordLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
