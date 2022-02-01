
package com.gp.health.ui.edit_profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.MemberTypeModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

import okhttp3.MultipartBody;

public class EditProfileViewModel extends BaseViewModel<EditProfileNavigator> {
    private MutableLiveData<DataWrapperModel<UserModel>> profileLiveData;
    private MutableLiveData<DataWrapperModel<UserModel>> updateProfileLiveData;
    private MutableLiveData<DataWrapperModel<Void>> checkOldPasswordLiveData;
    private MutableLiveData<DataWrapperModel<List<MemberTypeModel>>> memberTypesLiveData;

    public EditProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        profileLiveData = new MutableLiveData<>();
        updateProfileLiveData = new MutableLiveData<>();
        checkOldPasswordLiveData = new MutableLiveData<>();
        memberTypesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<UserModel>> getProfileLiveData() {
        return profileLiveData;
    }

    public LiveData<DataWrapperModel<UserModel>> getUpdateProfileLiveData() {
        return updateProfileLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getCheckOldPasswordLiveData() {
        return checkOldPasswordLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<MemberTypeModel>>> getMemberTypesLiveData() {
        return memberTypesLiveData;
    }

    public void getProfile() {
        getCompositeDisposable().add(getDataManager()
                .getProfileApiCall(getDataManager().getCurrentUserId())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
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

    public void updateProfile(MultipartBody.Builder builder) {

        getCompositeDisposable().add(getDataManager()
                .updateProfileApiCall(builder.build(), getDataManager().getCurrentUserId())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        UserModel userModel = response.getData();
                        userModel.setAccessToken(getDataManager().getAccessToken());
                        getDataManager().setUserObject(userModel);
                        updateProfileLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void checkOldPassword(String oldPassword) {
        getCompositeDisposable().add(getDataManager()
                .checkOldPasswordApiCall(getDataManager().getCurrentUserId(), oldPassword)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        checkOldPasswordLiveData.setValue(response);
                    } else if (response.getCode() == 400) {
                        getNavigator().wrongPassword();
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> getNavigator().handleError(throwable)));
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
