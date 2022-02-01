
package com.gp.health.ui.home;

import com.gp.health.data.models.LikeModel;

public interface HomeNavigator {

    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void favoriteDone(LikeModel data, int position);
}
