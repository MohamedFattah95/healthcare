
package com.gp.health.ui.adding_ad.ad_fees_info;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.SettingsModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class AdFeesInfoViewModel extends BaseViewModel<AdFeesInfoNavigator> {

    private MutableLiveData<DataWrapperModel<SettingsModel>> settingsLiveData;

    public AdFeesInfoViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        settingsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<SettingsModel>> getSettingsLiveData() {
        return settingsLiveData;
    }

    public void getSettings() {
        getCompositeDisposable().add(getDataManager()
                .getSettings()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        getDataManager().setSettingsObject(response.getData());
                        settingsLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
