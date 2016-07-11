package io.github.ryanhoo.firFlight.data.source.remote;

import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.data.source.UserContract;
import io.github.ryanhoo.firFlight.data.source.remote.api.RESTFulApiService;
import rx.Observable;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/2/16
 * Time: 10:44 PM
 * Desc: RemoteUserDataSource
 */
public class RemoteUserDataSource extends AbstractRemoteDataSource implements UserContract.Remote {

    public RemoteUserDataSource(RESTFulApiService api) {
        super(api);
    }

    @Override
    public Observable<User> user() {
        return mApi.user();
    }
}
