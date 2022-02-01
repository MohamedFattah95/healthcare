
package com.gp.health.ui.member_profile.member_ads;


import com.gp.health.data.models.LikeModel;

public interface MemberAdsNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void favoriteDone(LikeModel data, int position);
}
