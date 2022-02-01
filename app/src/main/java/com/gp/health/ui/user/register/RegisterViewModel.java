
package com.gp.health.ui.user.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.MemberTypeModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class RegisterViewModel extends BaseViewModel<RegisterNavigator> {

    private MutableLiveData<DataWrapperModel<UserModel>> registrationLiveData;
    private MutableLiveData<DataWrapperModel<List<MemberTypeModel>>> memberTypesLiveData;

    public RegisterViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        registrationLiveData = new MutableLiveData<>();
        memberTypesLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<UserModel>> getRegistrationLiveData() {
        return registrationLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<MemberTypeModel>>> getMemberTypesLiveData() {
        return memberTypesLiveData;
    }

    public void doRegistration(String name, String mobile, String password, int type) {

        getCompositeDisposable().add(getDataManager()
                .doRegistrationApiCall(name, mobile, password, type)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        registrationLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }


    public void getMemberTypes() {
        getCompositeDisposable().add(getDataManager()
                .getMemberTypesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        memberTypesLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> getNavigator().handleError(throwable)));
    }
}
