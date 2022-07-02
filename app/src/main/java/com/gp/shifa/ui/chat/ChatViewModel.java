
package com.gp.shifa.ui.chat;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.UserModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

public class ChatViewModel extends BaseViewModel<ChatNavigator> {
    private MutableLiveData<DataWrapperModel<Void>> chatLiveData;
    private MutableLiveData<DataWrapperModel<UserModel>> userInfoLiveData;

    public ChatViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        chatLiveData = new MutableLiveData<>();
        userInfoLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<Void>> getChatNotificationLiveData() {
        return chatLiveData;
    }

    public MutableLiveData<DataWrapperModel<UserModel>> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public void setUserInfoLiveData(MutableLiveData<DataWrapperModel<UserModel>> userInfoLiveData) {
        this.userInfoLiveData = userInfoLiveData;
    }

    //    public void sendChatNotification(int senderId, int receiverId) {
//        getCompositeDisposable().add(getDataManager()
//                .sendChatNotif(senderId, receiverId)
//                .subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(wrapperModel -> {
//                    chatLiveData.setValue(wrapperModel);
//                }, throwable -> {
//                    getNavigator().handleError(throwable);
//                }));
//    }

    public void getUserInfo(int receiverId) {
        getCompositeDisposable().add(getDataManager()
                .getProfileApiCall(receiverId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        userInfoLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
