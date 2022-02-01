
package com.gp.health.ui.mobile_search;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class MobileSearchViewModel extends BaseViewModel<MobileSearchNavigator> {

    private MutableLiveData<PagDataWrapperModel<List<UserModel>>> membersLiveData;

    public MobileSearchViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        membersLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PagDataWrapperModel<List<UserModel>>> getMembersLiveData() {
        return membersLiveData;
    }

    public void searchMembersByMobile(String mobile) {

        getCompositeDisposable().add(getDataManager()
                .searchMembersByMobileApiCall(mobile)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200 || response.getCode() == 206)
                        membersLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
