
package com.gp.shifa.ui.user.profile.blocked;


public interface BlockedNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void unBlocked(int position);
}
