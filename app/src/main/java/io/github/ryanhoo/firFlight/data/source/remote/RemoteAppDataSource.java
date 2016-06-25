package io.github.ryanhoo.firFlight.data.source.remote;

import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.source.AppContract;
import rx.Observable;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/26/16
 * Time: 12:44 AM
 * Desc: RemoteAppDataSource
 */
public class RemoteAppDataSource extends AbstractRemoteDataSource implements AppContract.Remote {

    public RemoteAppDataSource(RESTFulApiService api) {
        super(api);
    }

    @Override
    public Observable<List<App>> apps() {
        return mApi.apps();
    }
}
