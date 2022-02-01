
package com.gp.health.ui.profile.my_ads;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.HashMap;
import java.util.List;

public class MyAdsViewModel extends BaseViewModel<MyAdsNavigator> {

    private MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> userAdsLiveData;
    private MutableLiveData<DataWrapperModel<AdAndOrderModel>> adDetailsLiveData;

    public MyAdsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userAdsLiveData = new MutableLiveData<>();
        adDetailsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> getUserAdsLiveData() {
        return userAdsLiveData;
    }

    public MutableLiveData<DataWrapperModel<AdAndOrderModel>> getAdDetailsLiveData() {
        return adDetailsLiveData;
    }

    public void getUserAds(int userId, int type, int page) {
        getCompositeDisposable().add(getDataManager()
                .getUserAdsOrOrdersApiCall(userId, type, page)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200 || response.getCode() == 206)
                        userAdsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void deleteAd(HashMap<String, String> map, int position) {
        getCompositeDisposable().add(getDataManager()
                .deleteAdOrOrderApiCall(map)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        getNavigator().adDeleted(map.get("ids"), position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void getAdDetails(int adId) {
        getCompositeDisposable().add(getDataManager()
                .getAdOrOrderDetailsApiCall(adId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        adDetailsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
