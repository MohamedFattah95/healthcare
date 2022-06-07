
package com.gp.shifa.ui.user.complete_profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.CountriesAndAreasModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

import okhttp3.MultipartBody;

public class CompleteProfileViewModel extends BaseViewModel<CompleteProfileNavigator> {

    private MutableLiveData<DataWrapperModel<UserModel>> updateProfileLiveData;
    private MutableLiveData<DataWrapperModel<List<CountriesAndAreasModel>>> memberTypesLiveData;

    public CompleteProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        updateProfileLiveData = new MutableLiveData<>();
        memberTypesLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<UserModel>> getUpdateProfileLiveData() {
        return updateProfileLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<CountriesAndAreasModel>>> getMemberTypesLiveData() {
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

                    if (response.getStatus().equals("1")) {
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
                .getCountriesAndAreasApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1")) {
                        memberTypesLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> getNavigator().handleError(throwable)));
    }

}
