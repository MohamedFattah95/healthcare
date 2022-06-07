
package com.gp.shifa.ui.user.login;

public interface LoginNavigator {
    void openMainActivity();

    void handleError(Throwable throwable);

    void showMyApiMessage(String message);
}
