
package com.gp.health.ui.chats;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.ChatsModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class ChatsViewModel extends BaseViewModel<ChatsNavigator> {

    private MutableLiveData<DataWrapperModel<List<ChatsModel>>> chatsLiveData;

    public ChatsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        chatsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<ChatsModel>>> getUserChatsLiveData() {
        return chatsLiveData;
    }

    public void getUserChats() {
        getCompositeDisposable().add(getDataManager()
                .getUserChats(getDataManager().getCurrentUserId())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200 || response.getCode() == 206)
                        chatsLiveData.setValue(response);
                    else if (response.getCode() != 404)
                        getNavigator().showMyApiMessage(response.getMessage());
                    else
                        getNavigator().hideChatsLoading();

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
