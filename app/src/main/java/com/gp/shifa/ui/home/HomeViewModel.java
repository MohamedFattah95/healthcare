

package com.gp.shifa.ui.home;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.CategoriesModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.DoctorModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

public class HomeViewModel extends BaseViewModel<HomeNavigator> {

    private MutableLiveData<DataWrapperModel<List<CategoriesModel>>> categoriesLiveData;
    private MutableLiveData<DataWrapperModel<List<DoctorModel>>> doctorsLiveData;

    public HomeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        categoriesLiveData = new MutableLiveData<>();
        doctorsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<CategoriesModel>>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<DoctorModel>>> getDoctorsLiveData() {
        return doctorsLiveData;
    }

    public void getCategories() {
        getCompositeDisposable().add(getDataManager()
                .getCategoriesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        categoriesLiveData.setValue(response);
                    else
                        getCategories();

                }, throwable -> {
                    getCategories();
                }));
    }

    public void getDoctors() {
        getCompositeDisposable().add(getDataManager()
                .getDoctorsApiCall(1)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        doctorsLiveData.setValue(response);
                    else
                        getDoctors();

                }, throwable -> {
                    getDoctors();
                }));
    }
}
