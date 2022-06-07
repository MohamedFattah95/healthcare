

package com.gp.shifa.ui.favorites;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.AdAndOrderModel;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

public class FavoritesViewModel extends BaseViewModel<FavoritesNavigator> {

    private MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> favoritesLiveData;

    public FavoritesViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        favoritesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> getFavoritesLiveData() {
        return favoritesLiveData;
    }

    public void getFavorites(int userId, int page) {
        getCompositeDisposable().add(getDataManager()
                .getFavoritesApiCall(userId, page)
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

    public void unFavorite(int itemId, int position) {
        getCompositeDisposable().add(getDataManager()
                .favoriteOrUnFavoriteApiCall(itemId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        getNavigator().unFavoriteDone(position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
