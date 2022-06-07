
package com.gp.shifa.ui.member_profile.member_ads;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.AdAndOrderModel;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1"))
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

                    if (response.getStatus().equals("1"))
                        getNavigator().favoriteDone(response.getData(), position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
