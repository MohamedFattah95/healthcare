

package com.gp.health.ui.adding_ad.ad_location;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.GoogleShopWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.Map;

public class AdLocationViewModel extends BaseViewModel<AdLocationNavigator> {

    private MutableLiveData<GoogleShopWrapperModel> placeSearchLiveData;

    public AdLocationViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

        placeSearchLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<GoogleShopWrapperModel> getSearchPlacesLiveData() {
        return placeSearchLiveData;
    }


    public void getSearchPlaces(Map<String, String> map) {

        getCompositeDisposable().add(getDataManager()
                .getSearchPlaces(map)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("OK"))
                        placeSearchLiveData.setValue(response);

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
