package io.github.ryanhoo.firFlight.data.source.remote;

import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.AppInstallInfo;
import io.github.ryanhoo.firFlight.data.source.AppContract;
import io.github.ryanhoo.firFlight.data.source.remote.api.RESTFulApiService;
import io.github.ryanhoo.firFlight.network.AppResponse;
import rx.Observable;
import rx.functions.Func1;

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
        return mApi.apps().flatMap(new Func1<AppResponse, Observable<List<App>>>() {
            @Override
            public Observable<List<App>> call(AppResponse appResponse) {
                if (appResponse.getApps() != null) {
                    return Observable.just(appResponse.getApps());
                }
                return Observable.error(new Exception("No apps"));
            }
        });
    }

    @Override
    public Observable<AppInstallInfo> appInstallInfo(String appId) {
        return mApi.appInstallInfo(appId);
    }
}
