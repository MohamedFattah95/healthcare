
package com.gp.health.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.NotificationsModel;
import com.gp.health.data.models.PagDataWrapperModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class NotificationsViewModel extends BaseViewModel<NotificationsNavigator> {

    private MutableLiveData<PagDataWrapperModel<List<NotificationsModel>>> notificationsLiveData;
    private MutableLiveData<DataWrapperModel<Void>> notificationSeenLiveData;
    private MutableLiveData<DataWrapperModel<Void>> markAllAsReadLiveData;

    public NotificationsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        notificationsLiveData = new MutableLiveData<>();
        notificationSeenLiveData = new MutableLiveData<>();
        markAllAsReadLiveData = new MutableLiveData<>();
    }

    public LiveData<PagDataWrapperModel<List<NotificationsModel>>> getNotificationsLiveData() {
        return notificationsLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getNotificationSeenLiveData() {
        return notificationSeenLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getMarkAllAsReadLiveData() {
        return markAllAsReadLiveData;
    }

    public void getNotifications(int page) {
        getCompositeDisposable().add(getDataManager()
                .getNotificationsApiCall(getDataManager().getCurrentUserId(), page)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200 || response.getCode() == 206)
                        notificationsLiveData.setValue(response);
                    else if (response.getCode() != 404)
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void markNotificationAsSeen(int id) {
        getCompositeDisposable().add(getDataManager()
                .markNotificationAsSeenApiCall(id, "PUT")
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        notificationSeenLiveData.setValue(response);

                        UserModel userModel = getDataManager().getUserObject();
                        userModel.setUnseenNotificationsCount(userModel.getUnseenNotificationsCount() - 1);
                        getDataManager().setUserObject(userModel);
                    }


                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void markAllAsRead(int userId) {
        getCompositeDisposable().add(getDataManager()
                .markAllAsReadApiCall(userId, "PUT")
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        markAllAsReadLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
