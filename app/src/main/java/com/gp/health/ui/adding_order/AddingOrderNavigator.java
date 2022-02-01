
package com.gp.health.ui.adding_order;


public interface AddingOrderNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void noOptions();
}
