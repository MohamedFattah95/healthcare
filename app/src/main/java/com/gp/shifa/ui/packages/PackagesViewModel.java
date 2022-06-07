
package com.gp.shifa.ui.packages;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.SubscriptionPackagesModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

public class PackagesViewModel extends BaseViewModel<PackagesNavigator> {

    private MutableLiveData<DataWrapperModel<List<SubscriptionPackagesModel>>> packagesLiveData;

    public PackagesViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        packagesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<SubscriptionPackagesModel>>> getPackagesLiveData() {
        return packagesLiveData;
    }

    public void getPackages() {
        getCompositeDisposable().add(getDataManager()
                .getSubscriptionPackagesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        packagesLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
