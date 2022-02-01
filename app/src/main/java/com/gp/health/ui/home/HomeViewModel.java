

package com.gp.health.ui.home;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.CategoriesModel;
import com.gp.health.data.models.CityModel;
import com.gp.health.data.models.CityServiceModel;
import com.gp.health.data.models.CommercialsModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.HashMap;
import java.util.List;

public class HomeViewModel extends BaseViewModel<HomeNavigator> {

    private MutableLiveData<DataWrapperModel<List<CategoriesModel>>> categoriesLiveData;
    private MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> itemsLiveData;
    private MutableLiveData<DataWrapperModel<List<CommercialsModel>>> commercialsLiveData;
    private MutableLiveData<DataWrapperModel<CityModel>> cityServicesReviewsLiveData;
    private MutableLiveData<DataWrapperModel<List<CityServiceModel>>> cityServicesLiveData;
    private MutableLiveData<DataWrapperModel<Void>> addCityCommentLiveData;
    private MutableLiveData<DataWrapperModel<CityModel>> checkCityActiveLiveData;

    public HomeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        categoriesLiveData = new MutableLiveData<>();
        itemsLiveData = new MutableLiveData<>();
        commercialsLiveData = new MutableLiveData<>();
        cityServicesReviewsLiveData = new MutableLiveData<>();
        cityServicesLiveData = new MutableLiveData<>();
        addCityCommentLiveData = new MutableLiveData<>();
        checkCityActiveLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<DataWrapperModel<List<CategoriesModel>>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> getItemsLiveData() {
        return itemsLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<CommercialsModel>>> getCommercialsLiveData() {
        return commercialsLiveData;
    }

    public MutableLiveData<DataWrapperModel<CityModel>> getCityServicesReviewsLiveData() {
        return cityServicesReviewsLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<CityServiceModel>>> getCityServicesLiveData() {
        return cityServicesLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getAddCityCommentLiveData() {
        return addCityCommentLiveData;
    }

    public MutableLiveData<DataWrapperModel<CityModel>> getCheckCityActiveLiveData() {
        return checkCityActiveLiveData;
    }

    public void getCategories() {
        getCompositeDisposable().add(getDataManager()
                .getCategoriesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        categoriesLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void searchItems(HashMap<String, String> filterMap, int page) {

        getCompositeDisposable().add(getDataManager()
                .searchItemsApiCall(filterMap, page)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200 || response.getCode() == 206)
                        itemsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void searchCommercials(String ids) {

        getCompositeDisposable().add(getDataManager()
                .searchCommercialsApiCall(ids)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        commercialsLiveData.setValue(response);
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

    public void getCityServicesReviews(String arCurrentAreaAsId) {

        getCompositeDisposable().add(getDataManager()
                .getCityServicesReviewsApiCall(arCurrentAreaAsId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        cityServicesReviewsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void getCityServices() {

        getCompositeDisposable().add(getDataManager()
                .getCityServicesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        cityServicesLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void addCommentToCity(String city, String comment, int rating) {

        getCompositeDisposable().add(getDataManager()
                .addCommentToCityApiCall(city, comment, rating)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        addCityCommentLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void addCityServiceRate(String city, int cityServiceId, int rate) {

        getCompositeDisposable().add(getDataManager()
                .addCityServiceRateApiCall(city, cityServiceId, rate)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

//    public void isCityActive(String arCurrentAreaAsId) {
//
//        getCompositeDisposable().add(getDataManager()
//                .isCityActiveApiCall(arCurrentAreaAsId)
//                .subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(response -> {
//
//                    if (response.getCode() == 200)
//                        checkCityActiveLiveData.setValue(response);
//                    else
//                        getNavigator().showMyApiMessage(response.getMessage());
//
//                }, throwable -> {
//                    getNavigator().handleError(throwable);
//                }));
//
//    }
}
