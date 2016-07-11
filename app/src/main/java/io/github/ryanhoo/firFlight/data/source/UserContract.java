package io.github.ryanhoo.firFlight.data.source;

import io.github.ryanhoo.firFlight.data.model.User;
import rx.Observable;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/2/16
 * Time: 10:33 PM
 * Desc: UserContract
 */
public interface UserContract {

    interface Local {

        Observable<User> user();

        User _user();

        boolean save(User user);

        boolean delete(User user);

        int deleteAll();
    }

    interface Remote {

        Observable<User> user();
    }

    User restoreUser();

    Observable<User> user(boolean forceUpdate);
}
