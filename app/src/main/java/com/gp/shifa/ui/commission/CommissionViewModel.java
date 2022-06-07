
package com.gp.shifa.ui.commission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.models.BanksModel;
import com.gp.shifa.data.models.CommissionCalculatorModel;
import com.gp.shifa.data.models.DataWrapperModel;
import com.gp.shifa.ui.base.BaseViewModel;
import com.gp.shifa.utils.rx.SchedulerProvider;

import java.util.List;

import okhttp3.MultipartBody;

public class CommissionViewModel extends BaseViewModel<CommissionNavigator> {

    private MutableLiveData<DataWrapperModel<List<BanksModel>>> banksLiveData;
    private MutableLiveData<DataWrapperModel<Void>> submitCommissionLiveData;
    private MutableLiveData<DataWrapperModel<CommissionCalculatorModel>> commissionCalcLiveData;


    public CommissionViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        banksLiveData = new MutableLiveData<>();
        submitCommissionLiveData = new MutableLiveData<>();
        commissionCalcLiveData = new MutableLiveData<>();

    }

    public LiveData<DataWrapperModel<List<BanksModel>>> getBanksLiveData() {
        return banksLiveData;
    }

    public MutableLiveData<DataWrapperModel<Void>> getSubmitCommissionLiveData() {
        return submitCommissionLiveData;
    }

    public MutableLiveData<DataWrapperModel<CommissionCalculatorModel>> getCommissionCalcLiveData() {
        return commissionCalcLiveData;
    }

    public void getBanks() {
        getCompositeDisposable().add(getDataManager()
                .getBanksApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getStatus().equals("1"))
                        banksLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));
    }

    public void submitCommission(MultipartBody.Builder builder) {
        getCompositeDisposable().add(getDataManager()
                .submitCommissionApiCall(builder.build())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(wrapperModel -> submitCommissionLiveData.setValue(wrapperModel),
                        throwable -> getNavigator().handleError(throwable)));
    }

    public void calculateCommission(String amount) {
        getCompositeDisposable().add(getDataManager()
                .calculateCommissionApiCall(amount)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    if (response.getStatus().equals("1"))
                        commissionCalcLiveData.setValue(response);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());
                }, throwable -> getNavigator().handleError(throwable)));
    }
}
