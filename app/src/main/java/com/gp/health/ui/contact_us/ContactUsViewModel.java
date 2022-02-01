
package com.gp.health.ui.contact_us;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.ContactUsMessageTypesModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.HashMap;
import java.util.List;

public class ContactUsViewModel extends BaseViewModel<ContactUsNavigator> {

    private MutableLiveData<DataWrapperModel<List<ContactUsMessageTypesModel>>> messageTypesLiveData;
    private MutableLiveData<DataWrapperModel<Void>> contactUsMessageLiveData;

    public ContactUsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        messageTypesLiveData = new MutableLiveData<>();
        contactUsMessageLiveData = new MutableLiveData<>();
    }

    public LiveData<DataWrapperModel<List<ContactUsMessageTypesModel>>> getMessageTypesLiveData() {
        return messageTypesLiveData;
    }

    public LiveData<DataWrapperModel<Void>> getContactUsMessageLiveData() {
        return contactUsMessageLiveData;
    }

    public void getMessageTypes() {
        getCompositeDisposable().add(getDataManager()
                .getContactUsMessageTypesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        messageTypesLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> getNavigator().handleError(throwable)));

    }

    public void sendContactUsMessage(HashMap<String, String> map) {

        getCompositeDisposable().add(getDataManager()
                .sendContactUsMessageApiCall(map)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200) {
                        contactUsMessageLiveData.setValue(response);
                    } else {
                        getNavigator().showMyApiMessage(response.getMessage());
                    }

                }, throwable -> getNavigator().handleError(throwable)));
    }
}
