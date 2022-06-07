
package com.gp.shifa.ui.mobile_search;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1"))
                        membersLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
