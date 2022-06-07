
package com.gp.shifa.ui.chats;


public interface ChatsNavigator {
    void handleError(Throwable throwable);

    void showMyApiMessage(String message);

    void hideChatsLoading();
}
