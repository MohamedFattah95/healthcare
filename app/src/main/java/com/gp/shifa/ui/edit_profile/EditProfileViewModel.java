
package com.gp.shifa.ui.edit_profile;

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

public class EditProfileViewModel extends BaseViewModel<EditProfileNavigator> {
    private MutableLiveData<UserModel> profileLiveData;
    private MutableLiveData<DataWrapperModel<UserModel.UserBean>> updateProfileLiveData;
    private MutableLiveData<DataWrapperModel<List<CountriesAndAreasModel>>> countriesAndAreasLiveData;


    public EditProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        profileLiveData = new MutableLiveData<>();
        updateProfileLiveData = new MutableLiveData<>();
        countriesAndAreasLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<UserModel> getProfileLiveData() {
        return profileLiveData;
    }

    public LiveData<DataWrapperModel<UserModel.UserBean>> getUpdateProfileLiveData() {
        return updateProfileLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<CountriesAndAreasModel>>> getCountriesAndAreasLiveData() {
        return countriesAndAreasLiveData;
    }

    public void getProfile() {
        profileLiveData.setValue(getDataManager().getUserObject());
    }

    public void updateProfile(MultipartBody.Builder builder) {

        getCompositeDisposable().add(getDataManager()
                .updateProfileApiCall(builder.build())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1")) {
                        UserModel userModel = new UserModel();
                        UserModel.UserBean userModelBean = response.getData();
                        userModel.setAccessToken(getDataManager().getAccessToken());
                        userModel.setUser(userModelBean);
                        getDataManager().setUserObject(userModel);
                        updateProfileLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void getCountriesAndAreas() {
        getCompositeDisposable().add(getDataManager()
                .getCountriesAndAreasApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1")) {
                        countriesAndAreasLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> getNavigator().handleError(throwable)));
    }
}
