
package com.gp.shifa.ui.profile.my_ads;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.AdAndOrderModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1"))
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

                    if (response.getStatus().equals("1"))
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

                    if (response.getStatus().equals("1"))
                        adDetailsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
