
package com.gp.shifa.ui.search;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.CategoriesModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.data.models.SpecOptionModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

public class SearchViewModel extends BaseViewModel<SearchNavigator> {

    private MutableLiveData<DataWrapperModel<List<CategoriesModel>>> categoriesLiveData;
    private MutableLiveData<DataWrapperModel<List<SpecOptionModel>>> specOptionLiveData;

    public SearchViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        categoriesLiveData = new MutableLiveData<>();
        specOptionLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<CategoriesModel>>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public MutableLiveData<DataWrapperModel<List<SpecOptionModel>>> getSpecOptionLiveData() {
        return specOptionLiveData;
    }

    public void getCategories() {
        getCompositeDisposable().add(getDataManager()
                .getCategoriesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        categoriesLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void getSpecOptions(String category) {

        getCompositeDisposable().add(getDataManager()
                .getSpecOptionsApiCall(category)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        specOptionLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
