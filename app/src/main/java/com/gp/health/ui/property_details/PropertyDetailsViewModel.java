
package com.gp.health.ui.property_details;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.GoogleCategoriesModel;
import com.gp.health.data.models.GoogleShopWrapperModel;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

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

                    if (response.getCode() == 200)
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

                    if (response.getCode() == 200)
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

                    if (response.getCode() == 200)
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

                    if (response.getCode() == 200)
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

                    if (response.getCode() == 200)
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

                    if (response.getCode() == 200)
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
