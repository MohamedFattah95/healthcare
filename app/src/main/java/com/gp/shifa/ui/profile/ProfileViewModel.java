package com.gp.shifa.ui.profile;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

public class ProfileViewModel extends BaseViewModel<ProfileNavigator> {

    private MutableLiveData<DataWrapperModel<UserModel>> profileLiveData;

    public ProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        profileLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<DataWrapperModel<UserModel>> getProfileLiveData() {
        return profileLiveData;
    }

    public void getProfile() {
        getCompositeDisposable().add(getDataManager()
                .getProfileApiCall(getDataManager().getCurrentUserId())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1")) {
                        UserModel userModel = response.getData();
                        userModel.setAccessToken(getDataManager().getAccessToken());
                        getDataManager().setUserObject(userModel);
                        profileLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
