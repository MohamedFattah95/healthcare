
package com.gp.shifa.ui.doctors;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.DoctorModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DoctorsViewModel extends BaseViewModel<DoctorsNavigator> {

    private MutableLiveData<DataWrapperModel<List<DoctorModel>>> doctorsLiveData;

    public DoctorsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        doctorsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<DoctorModel>>> getDoctorsLiveData() {
        return doctorsLiveData;
    }

    public void getDoctors(int page) {
        AtomicInteger i = new AtomicInteger(page);
        getCompositeDisposable().add(getDataManager()
                .getDoctorsApiCall(page)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1")) {
                        doctorsLiveData.setValue(response);
                        if (!response.getData().isEmpty()) {
                            getDoctors(i.incrementAndGet());
                        }
                    } else
                        getDoctors(i.incrementAndGet());

                }, throwable -> {
                    getDoctors(i.incrementAndGet());
                }));
    }
}
