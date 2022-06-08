
package com.gp.shifa.ui.user.profile.my_ratings;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.CommentModel;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

public class MyRatingsViewModel extends BaseViewModel<MyRatingsNavigator> {

    private MutableLiveData<PagDataWrapperModel<List<CommentModel>>> userRatingsLiveData;

    public MyRatingsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
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

                    if (response.getStatus().equals("1"))
                        userRatingsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
