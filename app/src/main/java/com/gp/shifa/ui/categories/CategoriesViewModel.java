
package com.gp.shifa.ui.categories;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.CategoriesModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

public class CategoriesViewModel extends BaseViewModel<CategoriesNavigator> {

    private MutableLiveData<DataWrapperModel<List<CategoriesModel>>> categoriesLiveData;

    public CategoriesViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
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

                    if (response.getStatus().equals("1"))
                        categoriesLiveData.setValue(response);
                    else
                        getCategories();

                }, throwable -> {
                    getCategories();
                }));
    }
}
