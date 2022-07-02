
package com.gp.shifa.ui.category_doctors;

import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.CategoryDoctorsModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

public class CategoryDoctorsViewModel extends BaseViewModel<CategoryDoctorsNavigator> {

    private MutableLiveData<DataWrapperModel<CategoryDoctorsModel>> categoryDoctorsLiveData;

    public CategoryDoctorsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        categoryDoctorsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<CategoryDoctorsModel>> getCategoryDoctorsLiveData() {
        return categoryDoctorsLiveData;
    }


    public void getCategoryDoctors(int categoryId) {
        getCompositeDisposable().add(getDataManager()
                .getCategoryDoctorsApiCall(categoryId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        categoryDoctorsLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

}
