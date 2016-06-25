package io.github.ryanhoo.firFlight.data.source.remote;

import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.User;
import retrofit2.http.GET;
import rx.Observable;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 5/31/16
 * Time: 11:10 PM
 * Desc: RESTFulApiService
 */
public interface RESTFulApiService {

    // User
    @GET("/user")
    Observable<User> user();

    // Apps
    @GET("/apps")
    Observable<List<App>> apps();
}
