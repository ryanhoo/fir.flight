package io.github.ryanhoo.firFlight.data.service;

import io.github.ryanhoo.firFlight.data.model.Token;
import retrofit2.Call;
import retrofit2.http.*;

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
    @POST("user")
    Call<Token> login(@Field("email") String email, @Field("password") String password);

    @GET("/user/api_token")
    Call<Token> apiToken(@Query("access_token") String accessToken);

    // User
}
