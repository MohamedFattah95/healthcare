
package com.gp.health.ui.user.complete_profile;

import com.gp.health.data.models.UserModel;

public interface CompleteProfileNavigator {

    void setUserData(UserModel userModel);

    void showMyApiMessage(String message);

    void handleError(Throwable throwable);
}
