

package com.gp.shifa.ui.favorites;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.DoctorModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

public class FavoritesViewModel extends BaseViewModel<FavoritesNavigator> {

    private MutableLiveData<DataWrapperModel<List<DoctorModel>>> favoritesLiveData;

    public FavoritesViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        favoritesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<DoctorModel>>> getFavoritesLiveData() {
        return favoritesLiveData;
    }

    public void getFavorites() {
        getCompositeDisposable().add(getDataManager()
                .getFavoritesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        favoritesLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
