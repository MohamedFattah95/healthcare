
package com.gp.health.ui.adding_ad.select_ad_category;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.CategoriesModel;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;

public class SelectAdCategoryViewModel extends BaseViewModel<SelectAdCategoryNavigator> {

    private MutableLiveData<DataWrapperModel<List<CategoriesModel>>> categoriesLiveData;

    public SelectAdCategoryViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        categoriesLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<CategoriesModel>>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public void getCategories() {
        getCompositeDisposable().add(getDataManager()
                .getCategoriesApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        categoriesLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
