
package com.gp.shifa.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.NotificationsModel;
import com.gp.shifa.data.models.PagDataWrapperModel;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1"))
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

                    if (response.getStatus().equals("1")) {
                        notificationSeenLiveData.setValue(response);

                        UserModel userModel = getDataManager().getUserObject();
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

                    if (response.getStatus().equals("1"))
                        markAllAsReadLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
