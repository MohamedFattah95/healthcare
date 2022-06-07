
package com.gp.shifa.ui.user.complete_profile;

import com.gp.shifa.data.models.UserModel;

public interface CompleteProfileNavigator {

    void setUserData(UserModel userModel);

    void showMyApiMessage(String message);

    void handleError(Throwable throwable);
}
