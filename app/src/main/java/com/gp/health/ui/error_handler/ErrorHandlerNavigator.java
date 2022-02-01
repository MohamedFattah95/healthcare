
package com.gp.health.ui.error_handler;

public interface ErrorHandlerNavigator {

    void showMyApiMessage(String message);

    void handleError(Throwable throwable);

    void showUserDeletedMsg();
}
