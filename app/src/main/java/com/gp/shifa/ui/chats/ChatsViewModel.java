
package com.gp.shifa.ui.chats;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.ChatsModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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
                .getUserChats()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        chatsLiveData.setValue(response);
                    else
                        getNavigator().hideChatsLoading();

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
