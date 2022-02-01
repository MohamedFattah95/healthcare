
package com.gp.health.ui.adding_ad.add_ad_details;


public interface AddAdDetailsNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void noOptions();
}
