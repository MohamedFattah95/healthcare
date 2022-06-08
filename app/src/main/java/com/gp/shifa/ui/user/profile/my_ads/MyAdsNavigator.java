
package com.gp.shifa.ui.user.profile.my_ads;


public interface MyAdsNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void adDeleted(String adId, int position);
}
