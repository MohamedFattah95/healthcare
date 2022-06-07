
package com.gp.shifa.ui.property_details;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.AdAndOrderModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.GoogleCategoriesModel;
import com.gp.shifa.data.models.GoogleShopWrapperModel;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;
import java.util.Map;

public class PropertyDetailsViewModel extends BaseViewModel<PropertyDetailsNavigator> {

    private MutableLiveData<DataWrapperModel<AdAndOrderModel>> itemDetailsLiveData;
    private MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> similarItemsLiveData;
    private MutableLiveData<DataWrapperModel<Void>> reportItemLiveData;
    private MutableLiveData<DataWrapperModel<List<GoogleCategoriesModel>>> googleTypesLiveData;
    private MutableLiveData<GoogleShopWrapperModel> shopsLiveData;


    public PropertyDetailsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        itemDetailsLiveData = new MutableLiveData<>();
        similarItemsLiveData = new MutableLiveData<>();
        reportItemLiveData = new MutableLiveData<>();
        googleTypesLiveData = new MutableLiveData<>();
        shopsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<AdAndOrderModel>> getItemDetailsLiveData() {
        return itemDetailsLiveData;
    }

    public MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> getSimilarItemsLiveData() {
        return similarItemsLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getReportItemLiveData() {
        return reportItemLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<GoogleCategoriesModel>>> getGoogleTypesLiveData() {
        return googleTypesLiveData;
    }

    public MutableLiveData<GoogleShopWrapperModel> getShopsLiveData() {
        return shopsLiveData;
    }

    public void getItemDetails(int itemId) {

        getCompositeDisposable().add(getDataManager()
                .getAdOrOrderDetailsApiCall(itemId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        itemDetailsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void getSimilarItems(int categoryId, int itemId) {

        getCompositeDisposable().add(getDataManager()
                .getSimilarItemsApiCall(categoryId, itemId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        similarItemsLiveData.setValue(response);
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

    public void reportItem(int itemId, String commentText) {

        getCompositeDisposable().add(getDataManager()
                .reportItemApiCall(itemId, commentText)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        reportItemLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void doFavorite(int itemId) {

        getCompositeDisposable().add(getDataManager()
                .favoriteOrUnFavoriteApiCall(itemId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        getNavigator().favoriteDone(response.getData());
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void getGoogleServicesTypes() {
        getCompositeDisposable().add(getDataManager()
                .getGoogleServicesTypesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        googleTypesLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void getNearByServices(Map<String, String> map) {

        getCompositeDisposable().add(getDataManager()
                .getGoogleShops(map)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("OK"))
                        shopsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getStatus());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
