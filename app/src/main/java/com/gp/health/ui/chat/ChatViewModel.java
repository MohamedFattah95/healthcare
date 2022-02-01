
package com.gp.health.ui.chat;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.UserModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class ChatViewModel extends BaseViewModel<ChatNavigator> {
    private MutableLiveData<DataWrapperModel<Void>> chatLiveData;
    private MutableLiveData<DataWrapperModel<UserModel>> userInfoLiveData;
    private MutableLiveData<DataWrapperModel<String>> badWordsLiveData;

    public ChatViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        chatLiveData = new MutableLiveData<>();
        userInfoLiveData = new MutableLiveData<>();
        badWordsLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<Void>> getChatNotificationLiveData() {
        return chatLiveData;
    }

    public MutableLiveData<DataWrapperModel<UserModel>> getUserInfoLiveData() {
        return userInfoLiveData;
    }

    public MutableLiveData<DataWrapperModel<String>> getBadWordsLiveData() {
        return badWordsLiveData;
    }

    public void sendChatNotification(int senderId, int receiverId) {
        getCompositeDisposable().add(getDataManager()
                .sendChatNotif(senderId, receiverId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(wrapperModel -> {
                    chatLiveData.setValue(wrapperModel);
                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void getUserInfo(int receiverId) {
        getCompositeDisposable().add(getDataManager()
                .getProfileApiCall(receiverId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        userInfoLiveData.setValue(response);
                    else if (response.getCode() == 4001)
                        getNavigator().userIsBlocked(response.getMessage());
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void getBadWords() {
        getCompositeDisposable().add(getDataManager()
                .getBadWordsApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        badWordsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
