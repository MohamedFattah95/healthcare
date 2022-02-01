
package com.gp.health.ui.notifications;

public interface NotificationsNavigator {

    void handleError(Throwable throwable);

    void showMyApiMessage(String message);
}
