package io.github.ryanhoo.firFlight.data.service;

import io.github.ryanhoo.firFlight.data.model.*;
import io.github.ryanhoo.firFlight.network.MultiPageResponse;
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

    @GET("/apps/latest/{appId}")
    Call<AppInstallInfo> appInstallInfo(@Path("appId") String appId, @Query("api_token") String apiToken);

    // Notifications

    String NOTIFICATON_TYPE_SYS = "sys";
    String NOTIFICATION_TYPE_RELEASE = "release"; // TODO Multi pages when there are too many notifications

    @GET("/notifications")
    Call<MultiPageResponse<Message>> notifications(
            @Query("access_token") String accessToken,
            @Query("type") String type
    );

    @GET("/notifications?type=sys")
    Call<MultiPageResponse<Message>> systemNotifications(
            @Query("access_token") String accessToken
    );
}
