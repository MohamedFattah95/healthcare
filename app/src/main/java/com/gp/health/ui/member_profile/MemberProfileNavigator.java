
package com.gp.health.ui.member_profile;

public interface MemberProfileNavigator {

    void showMyApiMessage(String message);

    void handleError(Throwable throwable);

    void userIsBlocked(String message);
}
