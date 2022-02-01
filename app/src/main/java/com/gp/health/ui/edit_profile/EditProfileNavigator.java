
package com.gp.health.ui.edit_profile;


public interface EditProfileNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void wrongPassword();
}
