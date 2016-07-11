package io.github.ryanhoo.firFlight.data.source;

import io.github.ryanhoo.firFlight.data.Injection;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.data.source.local.LocalUserDataSource;
import io.github.ryanhoo.firFlight.data.source.remote.RemoteUserDataSource;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/2/16
 * Time: 10:45 PM
 * Desc: UserRepository
 */
public class UserRepository implements UserContract {

    private static UserRepository sInstance;

    UserContract.Local mLocalDataSource;
    UserContract.Remote mRemoteDataSource;

    private UserRepository() {
        mLocalDataSource = new LocalUserDataSource(Injection.provideContext());
        mRemoteDataSource = new RemoteUserDataSource(Injection.provideRESTfulApi());
    }

    public static UserRepository getInstance() {
        if (sInstance == null) {
            synchronized (UserRepository.class) {
                if (sInstance == null) {
                    sInstance = new UserRepository();
                }
            }
        }
        return sInstance;
    }

    @Override
    public User restoreUser() {
        return mLocalDataSource._user();
    }

    @Override
    public Observable<User> user(boolean forceUpdate) {
        Observable<User> remote = mRemoteDataSource.user().doOnNext(new Action1<User>() {
            @Override
            public void call(User user) {
                mLocalDataSource.delete(user);
                mLocalDataSource.save(user);
            }
        });
        if (forceUpdate) {
            return remote;
        }
        Observable<User> local = mLocalDataSource.user();
        return Observable.concat(local.first(), remote);
    }
}
