
package com.gp.shifa.ui.user.profile.blocked;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.BlockedModel;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1"))
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

                    if (response.getStatus().equals("1"))
                        getNavigator().unBlocked(position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
