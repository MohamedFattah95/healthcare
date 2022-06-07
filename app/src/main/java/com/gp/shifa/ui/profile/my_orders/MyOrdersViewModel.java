
package com.gp.shifa.ui.profile.my_orders;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.AdAndOrderModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1"))
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

                    if (response.getStatus().equals("1"))
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

                    if (response.getStatus().equals("1"))
                        orderDetailsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
