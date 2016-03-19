package io.github.ryanhoo.firFlight.data.service;

import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.model.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/15/16
 * Time: 11:06 PM
 * Desc: RetrofitService
 */
public interface RetrofitService {

    // Token

    @FormUrlEncoded
    @POST("/login")
    Call<Token> signIn(@Field("email") String email, @Field("password") String password);

    @GET("/user/api_token")
    Call<Token> apiToken(@Query("access_token") String accessToken);

    // User
    @GET("/user")
    Call<User> user(@Query("access_token") String accessToken);

    // Apps
    @GET("/apps")
    Call<List<App>> apps(@Query("access_token") String accessToken);
}
