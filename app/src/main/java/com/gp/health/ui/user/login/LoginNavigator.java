
package com.gp.health.ui.user.login;

import com.gp.health.data.models.DataWrapperModel;
import com.gp.health.data.models.UserModel;

public interface LoginNavigator {
    void openMainActivity();

    void handleError(Throwable throwable);

    void openVerificationActivity(DataWrapperModel<UserModel> response);

    void showMyApiMessage(String message);
}
