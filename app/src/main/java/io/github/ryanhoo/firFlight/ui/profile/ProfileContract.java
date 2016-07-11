package io.github.ryanhoo.firFlight.ui.profile;

import android.content.Context;
import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.ui.base.BasePresenter;
import io.github.ryanhoo.firFlight.ui.base.BaseView;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/9/16
 * Time: 12:21 AM
 * Desc: ProfileContract
 */
public interface ProfileContract {

    interface View extends BaseView<Presenter> {

        Context getContext();

        void updateUserProfile(User user);

        void updateApiToken(Token token);

        void onRefreshApiTokenStart();

        void onRefreshApiTokenCompleted();
    }

    interface Presenter extends BasePresenter {

        void refreshUserProfile();

        void refreshApiToken();
    }
}
