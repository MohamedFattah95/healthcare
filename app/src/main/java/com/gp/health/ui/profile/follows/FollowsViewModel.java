
package com.gp.health.ui.profile.follows;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.FollowerModel;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class FollowsViewModel extends BaseViewModel<FollowsNavigator> {

    private MutableLiveData<PagDataWrapperModel<List<FollowerModel>>> userFollowingsLiveData;

    public FollowsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userFollowingsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PagDataWrapperModel<List<FollowerModel>>> getUserFollowingsLiveData() {
        return userFollowingsLiveData;
    }

    public void getUserFollowings(int userId, int page) {
        getCompositeDisposable().add(getDataManager()
                .getUserFollowingsApiCall(userId, page)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200 || response.getCode() == 206)
                        userFollowingsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void unFollowUser(int followingId, int position) {
        getCompositeDisposable().add(getDataManager()
                .followOrUnFollowUserApiCall(followingId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        getNavigator().unFollowed(position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
