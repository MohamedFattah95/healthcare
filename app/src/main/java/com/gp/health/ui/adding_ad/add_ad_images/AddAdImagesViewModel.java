
package com.gp.health.ui.adding_ad.add_ad_images;

import com.gp.health.data.DataManager;
import com.gp.health.ui.base.BaseViewModel;
import com.gp.health.utils.rx.SchedulerProvider;

public class AddAdImagesViewModel extends BaseViewModel<AddAdImagesNavigator> {


    public AddAdImagesViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public void removeImage(int mediaId, int position) {

        getCompositeDisposable().add(getDataManager()
                .deleteMediaApiCall(mediaId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        getNavigator().imageDeleted(position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());


                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }

    public void removeVideo(int mediaId, int position) {

        getCompositeDisposable().add(getDataManager()
                .deleteMediaApiCall(mediaId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {

                    if (response.getCode() == 200)
                        getNavigator().videoDeleted(position);
                    else
                        getNavigator().showMyApiMessage(response.getMessage());

                }, throwable -> {
                    getNavigator().handleError(throwable);
                }));

    }
}
