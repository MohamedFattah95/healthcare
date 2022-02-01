
package com.gp.health.ui.profile.blocked;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.BlockedModel;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class BlockedViewModel extends BaseViewModel<BlockedNavigator> {

    private MutableLiveData<PagDataWrapperModel<List<BlockedModel>>> userBlockedLiveData;

    public BlockedViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userBlockedLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PagDataWrapperModel<List<BlockedModel>>> getUserBlockedLiveData() {
        return userBlockedLiveData;
    }

    public void getUserBlocked(int userId, int page) {

        getCompositeDisposable().add(getDataManager()
                .getUserBlockedApiCall(userId, page)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200 || response.getCode() == 206)
                        userBlockedLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void unBlockUser(int userId, int position) {

        getCompositeDisposable().add(getDataManager()
                .blockOrUnblockUserApiCall(userId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        getNavigator().unBlocked(position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
