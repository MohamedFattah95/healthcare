

package com.gp.health.ui.user.verify_code;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class VerifyCodeViewModel extends BaseViewModel<VerifyCodeNavigator> {

    private MutableLiveData<DataWrapperModel<Void>> verifyCodePasswordLiveData;
    private MutableLiveData<DataWrapperModel<Integer>> resendCodeLiveData;

    public VerifyCodeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        verifyCodePasswordLiveData = new MutableLiveData<>();
        resendCodeLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<Void>> getVerifyCodePasswordLiveData() {
        return verifyCodePasswordLiveData;
    }

    public LiveData<DataWrapperModel<Integer>> getResendCodeLiveData() {
        return resendCodeLiveData;
    }

    public void verifyCodePassword(String phone, String code) {
        getCompositeDisposable().add(getDataManager()
                .verifyCodePasswordApiCall(phone, code)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        verifyCodePasswordLiveData.setValue(response);
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

}
