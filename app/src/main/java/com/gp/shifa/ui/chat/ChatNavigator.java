
package com.gp.shifa.ui.chat;


public interface ChatNavigator {
    void handleError(Throwable throwable);

    void openOrderDetailsActivity();

    void userIsBlocked(String message);

    void showMyApiMessage(String message);
}
