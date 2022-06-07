
package com.gp.shifa.ui.user.forgot_password;


public interface ForgotPasswordNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);
}
