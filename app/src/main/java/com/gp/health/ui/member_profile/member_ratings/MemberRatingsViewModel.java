
package com.gp.health.ui.member_profile.member_ratings;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.CommentModel;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class MemberRatingsViewModel extends BaseViewModel<MemberRatingsNavigator> {

    private MutableLiveData<PagDataWrapperModel<List<CommentModel>>> userRatingsLiveData;


    public MemberRatingsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        userRatingsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PagDataWrapperModel<List<CommentModel>>> getUserRatingsLiveData() {
        return userRatingsLiveData;
    }

    public void getUserRatings(int userId, int page) {
        getCompositeDisposable().add(getDataManager()
                .getUserRatingsApiCall(userId, page)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200 || response.getCode() == 206)
                        userRatingsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }


}
