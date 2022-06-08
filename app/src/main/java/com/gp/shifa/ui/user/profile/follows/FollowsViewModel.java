
package com.gp.shifa.ui.user.profile.follows;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.FollowerModel;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1"))
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

                    if (response.getStatus().equals("1"))
                        getNavigator().unFollowed(position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
