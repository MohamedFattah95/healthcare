
package com.gp.health.ui.favorites;

public interface FavoritesNavigator {

    void showMyApiMessage(String message);

    void handleError(Throwable throwable);

    void unFavoriteDone(int position);
}
