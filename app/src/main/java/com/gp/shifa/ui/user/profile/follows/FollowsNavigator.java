
package com.gp.shifa.ui.user.profile.follows;


public interface FollowsNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void unFollowed(int position);
}
