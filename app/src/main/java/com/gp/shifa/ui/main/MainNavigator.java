
package com.gp.shifa.ui.main;


public interface MainNavigator {

    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void openLoginActivity();

}
