
package com.gp.health.ui.property_details;


import com.gp.health.data.models.LikeModel;

public interface PropertyDetailsNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void favoriteDone(LikeModel data, int position);

    void favoriteDone(LikeModel data);
}
