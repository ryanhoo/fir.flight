package io.github.ryanhoo.firFlight.data.source;

import io.github.ryanhoo.firFlight.data.Injection;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.AppInstallInfo;
import io.github.ryanhoo.firFlight.data.source.local.LocalAppDataSource;
import io.github.ryanhoo.firFlight.data.source.remote.RemoteAppDataSource;
import rx.Observable;
import rx.functions.Action1;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/26/16
 * Time: 12:47 AM
 * Desc: AppRepository
 */
public class AppRepository implements AppContract {

    private static AppRepository sInstance;

    AppContract.Local mLocalDataSource;
    AppContract.Remote mRemoteDataSource;

    private AppRepository() {
        mLocalDataSource = new LocalAppDataSource(Injection.provideContext());
        mRemoteDataSource = new RemoteAppDataSource(Injection.provideRESTfulApi());
    }

    public static AppRepository getInstance() {
        if (sInstance == null) {
            synchronized (AppRepository.class) {
                if (sInstance == null) {
                    sInstance = new AppRepository();
                }
            }
        }
        return sInstance;
    }

    @Override
    public Observable<List<App>> apps(boolean forceUpdate) {
        Observable<List<App>> local = mLocalDataSource.apps();
        Observable<List<App>> remote = mRemoteDataSource.apps()
                .doOnNext(new Action1<List<App>>() {
                    @Override
                    public void call(List<App> apps) {
                        mLocalDataSource.deleteAll();
                        mLocalDataSource.save(apps);
                    }
                });
        if (forceUpdate) {
            return remote;
        }
        return Observable.concat(local.first(), remote);
    }

    @Override
    public Observable<AppInstallInfo> appInstallInfo(String appId) {
        return mRemoteDataSource.appInstallInfo(appId);
    }
}
