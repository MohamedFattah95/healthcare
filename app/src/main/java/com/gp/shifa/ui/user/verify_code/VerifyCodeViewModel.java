

package com.gp.shifa.ui.user.verify_code;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1")) {
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

                    if (response.getStatus().equals("1")) {
                        resendCodeLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
