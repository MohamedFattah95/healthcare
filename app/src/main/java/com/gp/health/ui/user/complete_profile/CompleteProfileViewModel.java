
package com.gp.health.ui.user.complete_profile;

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

public class CompleteProfileViewModel extends BaseViewModel<CompleteProfileNavigator> {

    private MutableLiveData<DataWrapperModel<UserModel>> updateProfileLiveData;
    private MutableLiveData<DataWrapperModel<List<MemberTypeModel>>> memberTypesLiveData;

    public CompleteProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        updateProfileLiveData = new MutableLiveData<>();
        memberTypesLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<UserModel>> getUpdateProfileLiveData() {
        return updateProfileLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<MemberTypeModel>>> getMemberTypesLiveData() {
        return memberTypesLiveData;
    }

    public void getUserDate() {
        getNavigator().setUserData(getDataManager().getUserObject());
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
