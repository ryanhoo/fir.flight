package io.github.ryanhoo.firFlight.network;

import android.util.Log;
import io.github.ryanhoo.firFlight.account.UserSession;
import io.github.ryanhoo.firFlight.data.model.Token;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 4/2/16
 * Time: 1:05 AM
 * Desc: SessionInterceptor
 */
public class SessionInterceptor implements Interceptor {

    private static final String TAG = "SessionInterceptor";

    /**
     * Access token conflicts with api token, they can't be set in a same request.
     * But some apis(fir's public apis) relay on api token, so it depends on a query parameter attached
     * with the request url, which is called 'requireApiToken'.
     * It's default false for sure, when api token is required, you must declare it in the
     * retrofit service's path.
     * Such as:
     * \@GET("/apps/latest/{appId}?requireApiToken=true")
     * Call<AppInstallInfo> appInstallInfo(@Path("appId") String appId);
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Token token = UserSession.getInstance().getToken();
        if (token != null) {
            HttpUrl httpUrl = request.url();
            boolean isApiTokenRequired = false;
            try {
                isApiTokenRequired = Boolean.parseBoolean(httpUrl.queryParameter("requireApiToken"));
            } catch (Exception ignore) {
            }
            if (isApiTokenRequired) {
                httpUrl = httpUrl.newBuilder()
                        .addQueryParameter("api_token", token.getApiToken())
                        .build();
                request = request.newBuilder().url(httpUrl).build();
            } else {
                request = request.newBuilder()
                        .addHeader("AccessToken", token.getAccessToken())
                        .build();
                Log.d(TAG, "AccessToken: " + token.getAccessToken());
            }
        }
        Log.d(TAG, "Send request: " + request.url());
        return chain.proceed(request);
    }
}
