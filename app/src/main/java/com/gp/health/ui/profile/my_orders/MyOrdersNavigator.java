
package com.gp.health.ui.profile.my_orders;


public interface MyOrdersNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void orderDeleted(String orderId, int position);
}
