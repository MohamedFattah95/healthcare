
package com.gp.health.ui.profile.my_orders;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.HashMap;
import java.util.List;

public class MyOrdersViewModel extends BaseViewModel<MyOrdersNavigator> {

    private MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> userOrdersLiveData;
    private MutableLiveData<DataWrapperModel<AdAndOrderModel>> orderDetailsLiveData;

    public MyOrdersViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userOrdersLiveData = new MutableLiveData<>();
        orderDetailsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PagDataWrapperModel<List<AdAndOrderModel>>> getUserOrdersLiveData() {
        return userOrdersLiveData;
    }

    public MutableLiveData<DataWrapperModel<AdAndOrderModel>> getOrderDetailsLiveData() {
        return orderDetailsLiveData;
    }

    public void getUserOrders(int userId, int type, int page) {
        getCompositeDisposable().add(getDataManager()
                .getUserAdsOrOrdersApiCall(userId, type, page)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200 || response.getCode() == 206)
                        userOrdersLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void deleteOrder(HashMap<String, String> map, int position) {

        getCompositeDisposable().add(getDataManager()
                .deleteAdOrOrderApiCall(map)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        getNavigator().orderDeleted(map.get("ids"), position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void getOrderDetails(int orderId) {

        getCompositeDisposable().add(getDataManager()
                .getAdOrOrderDetailsApiCall(orderId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        orderDetailsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
