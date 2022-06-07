
package com.gp.shifa.ui.user.register;

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

public class RegisterViewModel extends BaseViewModel<RegisterNavigator> {

    private MutableLiveData<DataWrapperModel<UserModel>> registrationLiveData;
    private MutableLiveData<DataWrapperModel<List<CountriesAndAreasModel>>> countriesAndAreasLiveData;

    public RegisterViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        registrationLiveData = new MutableLiveData<>();
        countriesAndAreasLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<UserModel>> getRegistrationLiveData() {
        return registrationLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<CountriesAndAreasModel>>> getCountriesAndAreasLiveData() {
        return countriesAndAreasLiveData;
    }

    public void doRegistration(MultipartBody.Builder builder) {

        getCompositeDisposable().add(getDataManager()
                .doRegistrationApiCall(builder.build())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1")) {
                        registrationLiveData.setValue(response);
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
