
package com.gp.shifa.ui.add_commercial;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.SubscriptionPackagesModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

import okhttp3.MultipartBody;

public class AddCommercialViewModel extends BaseViewModel<AddCommercialNavigator> {

    private MutableLiveData<DataWrapperModel<Integer>> submitCommercialActivityLiveData;
    private MutableLiveData<DataWrapperModel<List<SubscriptionPackagesModel>>> subscriptionPackagesLiveData;

    public AddCommercialViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        submitCommercialActivityLiveData = new MutableLiveData<>();
        subscriptionPackagesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<Integer>> getSubmitCommercialActivityLiveData() {
        return submitCommercialActivityLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<SubscriptionPackagesModel>>> getSubscriptionPackagesLiveData() {
        return subscriptionPackagesLiveData;
    }

    public void submitCommercialActivity(MultipartBody.Builder builder) {

        getCompositeDisposable().add(getDataManager()
                .submitCommercialActivityApiCall(builder.build())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> submitCommercialActivityLiveData.setValue(response),
                        throwable -> getNavigator().handleError(throwable)));

    }

    public void getSubscriptionPackages() {

        getCompositeDisposable().add(getDataManager()
                .getSubscriptionPackagesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                            if (response.getStatus().equals("1"))
                                subscriptionPackagesLiveData.setValue(response);
                            else
                                getNavigator().showMyApiMessage(response.getMessage());

                        },
                        throwable -> getNavigator().handleError(throwable)));

    }
}
