package io.github.ryanhoo.firFlight.data.source;

import android.support.annotation.NonNull;
import io.github.ryanhoo.firFlight.FlightApplication;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.data.source.local.LocalDataSource;
import io.github.ryanhoo.firFlight.data.source.remote.RemoteDataSource;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 5/31/16
 * Time: 11:52 AM
 * Desc: AppsRepository
 */
public class DataRepository implements DataSourceContract {

    private static DataRepository sInstance;

    private LocalDataSource mLocalDataSource;
    private RemoteDataSource mRemoteDataSource;

    private DataRepository(@NonNull LocalDataSource localDataSource, @NonNull RemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    public static DataRepository getInstance() {
        if (sInstance == null) {
            sInstance = new DataRepository(
                    LocalDataSource.getInstance(FlightApplication.getInstance()),
                    RemoteDataSource.getInstance()
            );
        }
        return sInstance;
    }

    @Override
    public Observable<User> user() {
        return mRemoteDataSource.user();
    }

    @Override
    public Observable<List<App>> apps() {
        Observable<List<App>> localApps = mLocalDataSource.apps();
        Observable<List<App>> remoteApps = mRemoteDataSource.apps()
                .flatMap(new Func1<List<App>, Observable<App>>() {
                    @Override
                    public Observable<App> call(List<App> apps) {
                        return Observable.from(apps);
                    }
                })
                .doOnNext(new Action1<App>() {
                    @Override
                    public void call(App app) {
                        mLocalDataSource.saveApp(app);
                    }
                })
                .toList();
        return Observable.concat(localApps.first(), remoteApps);
    }
}
