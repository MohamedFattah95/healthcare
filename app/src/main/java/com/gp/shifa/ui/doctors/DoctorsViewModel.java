
package com.gp.shifa.ui.doctors;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.CityModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

public class DoctorsViewModel extends BaseViewModel<DoctorsNavigator> {

    private MutableLiveData<DataWrapperModel<List<CityModel>>> rootCitiesLiveData;

    public DoctorsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        rootCitiesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<CityModel>>> getRootCitiesLiveData() {
        return rootCitiesLiveData;
    }

    public void getActiveAreas() {
        getCompositeDisposable().add(getDataManager()
                .getRootCitiesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        rootCitiesLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
