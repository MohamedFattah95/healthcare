
package com.gp.health.ui.adding_ad.add_ad_images;


public interface AddAdImagesNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void imageDeleted(int position);

    void videoDeleted(int position);
}
