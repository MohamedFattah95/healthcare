
package com.gp.shifa.ui.doctor_details;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.DoctorDetailsModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

public class DoctorDetailsViewModel extends BaseViewModel<DoctorDetailsNavigator> {

    private MutableLiveData<DataWrapperModel<DoctorDetailsModel>> doctorDetailsLiveData;
    private MutableLiveData<DataWrapperModel<Void>> favLiveData;

    public DoctorDetailsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        doctorDetailsLiveData = new MutableLiveData<>();
        favLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<DoctorDetailsModel>> getDoctorDetailsLiveData() {
        return doctorDetailsLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getFavLiveData() {
        return favLiveData;
    }

    public void getDoctorDetails(int doctorId) {
        getCompositeDisposable().add(getDataManager()
                .getDoctorDetailsApiCall(doctorId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        doctorDetailsLiveData.setValue(response);
                    else
                        getDoctorDetails(doctorId);

                }, throwable -> {
                    getDoctorDetails(doctorId);
                }));
    }

    public void doFavorite(int id) {
        getCompositeDisposable().add(getDataManager()
                .favoriteOrUnFavoriteApiCall(id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        favLiveData.setValue(response);

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
