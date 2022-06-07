
package com.gp.shifa.ui.faqs;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.FAQsModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

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

                    if (response.getStatus().equals("1"))
                        faqsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }
}
