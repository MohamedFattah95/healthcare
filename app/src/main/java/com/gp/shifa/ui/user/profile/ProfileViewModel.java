package com.gp.shifa.ui.user.profile;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

public class ProfileViewModel extends BaseViewModel<ProfileNavigator> {

    private MutableLiveData<UserModel> profileLiveData;

    public ProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        profileLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<UserModel> getProfileLiveData() {
        return profileLiveData;
    }

    public void getProfile() {
        profileLiveData.setValue(getDataManager().getUserObject());
    }
}
