
package com.gp.shifa.ui.user.change_password;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

public class ChangePasswordViewModel extends BaseViewModel<ChangePasswordNavigator> {
    private MutableLiveData<DataWrapperModel<UserModel>> changePasswordLiveData;

    public ChangePasswordViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        changePasswordLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<UserModel>> getChangePasswordLiveData() {
        return changePasswordLiveData;
    }

    public void changePassword(String oldPassword, String newPassword) {
        getCompositeDisposable().add(getDataManager()
                .resetPasswordApiCall(oldPassword, newPassword)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1")) {
                        getDataManager().setUserObject(response.getData());
                        getDataManager().updateUserInfo(response.getData(), DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                        getDataManager().setAccessToken(response.getData().getAccessToken());
                        changePasswordLiveData.setValue(response);
                    } else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
