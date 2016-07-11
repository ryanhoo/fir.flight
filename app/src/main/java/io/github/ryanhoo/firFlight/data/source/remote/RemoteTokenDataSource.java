package io.github.ryanhoo.firFlight.data.source.remote;

import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.source.TokenContract;
import io.github.ryanhoo.firFlight.data.source.remote.api.RESTFulApiService;
import rx.Observable;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/3/16
 * Time: 9:52 PM
 * Desc: RemoteTokenDataSource
 */
public class RemoteTokenDataSource extends AbstractRemoteDataSource implements TokenContract.Remote {

    public RemoteTokenDataSource(RESTFulApiService api) {
        super(api);
    }

    @Override
    public Observable<Token> accessToken(String email, String password) {
        return mApi.accessToken(email, password);
    }

    @Override
    public Observable<Token> apiToken() {
        return mApi.apiToken();
    }

    @Override
    public Observable<Token> refreshApiToken() {
        return mApi.refreshApiToken();
    }
}
