
package com.gp.shifa.ui.property_details;


import com.gp.shifa.data.models.LikeModel;

public interface PropertyDetailsNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void favoriteDone(LikeModel data, int position);

    void favoriteDone(LikeModel data);
}
