
package com.gp.health.ui.member_profile;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class MemberProfileViewModel extends BaseViewModel<MemberProfileNavigator> {

    private MutableLiveData<DataWrapperModel<UserModel>> profileLiveData;
    private MutableLiveData<DataWrapperModel<Void>> blockUserLiveData;
    private MutableLiveData<DataWrapperModel<Void>> followUserLiveData;
    private MutableLiveData<DataWrapperModel<Void>> rateUserLiveData;

    public MemberProfileViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        profileLiveData = new MutableLiveData<>();
        blockUserLiveData = new MutableLiveData<>();
        followUserLiveData = new MutableLiveData<>();
        rateUserLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<UserModel>> getProfileLiveData() {
        return profileLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getBlockUserLiveData() {
        return blockUserLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getFollowUserLiveData() {
        return followUserLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getRateUserLiveData() {
        return rateUserLiveData;
    }

    public void getMemberDetails(int userId) {

        getCompositeDisposable().add(getDataManager()
                .getProfileApiCall(userId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        profileLiveData.setValue(response);
                    } else if (response.getCode() == 4001) {
                        getNavigator().userIsBlocked(response.getMessage());
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void blockUser(int userId) {
        getCompositeDisposable().add(getDataManager()
                .blockOrUnblockUserApiCall(userId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        blockUserLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void followOrUnFollowUser(int userId) {
        getCompositeDisposable().add(getDataManager()
                .followOrUnFollowUserApiCall(userId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        followUserLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }


    public void submitMemberRate(int userId, String comment, int rating) {

        getCompositeDisposable().add(getDataManager()
                .rateUserApiCall(userId, comment, rating)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        rateUserLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
