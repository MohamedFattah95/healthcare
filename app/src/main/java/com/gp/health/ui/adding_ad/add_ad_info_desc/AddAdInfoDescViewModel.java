
package com.gp.health.ui.adding_ad.add_ad_info_desc;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.AdAndOrderModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import okhttp3.MultipartBody;

public class AddAdInfoDescViewModel extends BaseViewModel<AddAdInfoDescNavigator> {

    private MutableLiveData<DataWrapperModel<AdAndOrderModel>> submitAdLiveData;
    private MutableLiveData<DataWrapperModel<Void>> updateAdLiveData;

    public AddAdInfoDescViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        submitAdLiveData = new MutableLiveData<>();
        updateAdLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<AdAndOrderModel>> getSubmitAdLiveData() {
        return submitAdLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getUpdateAdLiveData() {
        return updateAdLiveData;
    }

    public void submitAd(MultipartBody.Builder builder) {

        getCompositeDisposable().add(getDataManager()
                .submitAdOrOrderApiCall(builder.build())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        submitAdLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable ->
                        getNavigator().handleError(throwable)));

    }

    public void updateAd(int adId, MultipartBody.Builder builder) {

        getCompositeDisposable().add(getDataManager()
                .updateAdOrOrderApiCall(adId, builder.build())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        updateAdLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable ->
                        getNavigator().handleError(throwable)));

    }
}
