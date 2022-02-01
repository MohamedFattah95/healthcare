
package com.gp.health.ui.member_profile.member_ads;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class MemberAdsViewModel extends BaseViewModel<MemberAdsNavigator> {

    private MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> userAdsLiveData;

    public MemberAdsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userAdsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> getUserAdsLiveData() {
        return userAdsLiveData;
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


    public void favoriteOrUnFavorite(int itemId, int position) {

        getCompositeDisposable().add(getDataManager()
                .favoriteOrUnFavoriteApiCall(itemId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        getNavigator().favoriteDone(response.getData(), position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
