package com.gp.health.ui.intro;

import androidx.lifecycle.MutableLiveData;

import com.gp.health.data.DataManager;
import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.IntroModel;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

import java.util.List;


public class IntroViewModel extends BaseViewModel<IntroNavigator> {

    private MutableLiveData<DataWrapperModel<List<IntroModel>>> introLiveData;

    public IntroViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        introLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<DataWrapperModel<List<IntroModel>>> getIntroLiveData() {
        return introLiveData;
    }

    public void getHowToUse() {
        getCompositeDisposable().add(getDataManager()
                .getHowToUseApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        introLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }


}
