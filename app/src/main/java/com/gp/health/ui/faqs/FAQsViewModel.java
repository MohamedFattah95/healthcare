
package com.gp.health.ui.faqs;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.FAQsModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class FAQsViewModel extends BaseViewModel<FAQsNavigator> {

    private MutableLiveData<DataWrapperModel<List<FAQsModel>>> faqsLiveData;

    public FAQsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        faqsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<FAQsModel>>> getFaqsLiveData() {
        return faqsLiveData;
    }

    public void getFAQs() {
        getCompositeDisposable().add(getDataManager()
                .getFAQsApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        faqsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
