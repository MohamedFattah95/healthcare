
package com.gp.shifa.ui.notifications;

public interface NotificationsNavigator {

    void handleError(Throwable throwable);

    void showMyApiMessage(String message);
}
