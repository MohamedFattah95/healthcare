
package com.gp.shifa.ui.home;

import com.gp.shifa.data.models.LikeModel;

public interface HomeNavigator {

    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void favoriteDone(LikeModel data, int position);
}
