
package com.gp.health.ui.adding_ad.add_ad_details;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.SpecOptionModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class AddAdDetailsViewModel extends BaseViewModel<AddAdDetailsNavigator> {

    private MutableLiveData<DataWrapperModel<List<SpecOptionModel>>> specOptionLiveData;

    public AddAdDetailsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        specOptionLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<SpecOptionModel>>> getSpecOptionLiveData() {
        return specOptionLiveData;
    }

    public void getSpecOptions(int categoryId) {

        getCompositeDisposable().add(getDataManager()
                .getSpecOptionsApiCall(String.valueOf(categoryId))
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
}
