package io.github.ryanhoo.firFlight.data.source.remote;

import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.data.source.DataSourceContract;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import rx.Observable;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 5/31/16
 * Time: 8:42 PM
 * Desc: RemoteDataSource
 */
public class RemoteDataSource implements DataSourceContract {

    private static RemoteDataSource sInstance;
    ApiService mApi;

    private RemoteDataSource() {
        mApi = RetrofitClient.defaultInstance().create(ApiService.class);
    }

    public static RemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new RemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public Observable<User> user() {
        return mApi.user();
    }

    @Override
    public Observable<List<App>> apps() {
        return mApi.apps();
    }

}
