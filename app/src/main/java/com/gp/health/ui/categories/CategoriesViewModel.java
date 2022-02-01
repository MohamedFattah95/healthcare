
package com.gp.health.ui.categories;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.CityModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class CategoriesViewModel extends BaseViewModel<CategoriesNavigator> {

    private MutableLiveData<DataWrapperModel<List<CityModel>>> rootCitiesLiveData;

    public CategoriesViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
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

                    if (response.getCode() == 200 || response.getCode() == 206)
                        rootCitiesLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
