
package com.gp.health.ui.adding_order;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.CategoriesModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.SpecOptionModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

import okhttp3.MultipartBody;

public class AddingOrderViewModel extends BaseViewModel<AddingOrderNavigator> {

    private MutableLiveData<DataWrapperModel<List<CategoriesModel>>> categoriesLiveData;
    private MutableLiveData<DataWrapperModel<List<SpecOptionModel>>> specOptionLiveData;
    private MutableLiveData<DataWrapperModel<AdAndOrderModel>> submitOrderLiveData;
    private MutableLiveData<DataWrapperModel<Void>> updateOrderLiveData;

    public AddingOrderViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        categoriesLiveData = new MutableLiveData<>();
        specOptionLiveData = new MutableLiveData<>();
        submitOrderLiveData = new MutableLiveData<>();
        updateOrderLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<CategoriesModel>>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<SpecOptionModel>>> getSpecOptionLiveData() {
        return specOptionLiveData;
    }

    public MutableLiveData<DataWrapperModel<AdAndOrderModel>> getSubmitOrderLiveData() {
        return submitOrderLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getUpdateOrderLiveData() {
        return updateOrderLiveData;
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

    public void getSpecOptions(String category) {

        getCompositeDisposable().add(getDataManager()
                .getSpecOptionsApiCall(category)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        specOptionLiveData.setValue(response);
                    else if (response.getCode() == 404)
                        getNavigator().noOptions();
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void submitOrder(MultipartBody.Builder builder) {

        getCompositeDisposable().add(getDataManager()
                .submitAdOrOrderApiCall(builder.build())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        submitOrderLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable ->
                        getNavigator().handleError(throwable)));


    }

    public void updateOrder(int orderId, MultipartBody.Builder builder) {

        getCompositeDisposable().add(getDataManager()
                .updateAdOrOrderApiCall(orderId, builder.build())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        updateOrderLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable ->
                        getNavigator().handleError(throwable)));

    }
}
