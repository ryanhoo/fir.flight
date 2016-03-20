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

    String QUERY_KEY_ACCESS_TOKEN = "access_token";
    String QUERY_KEY_API_TOKEN = "api_token";

    // Token

    @FormUrlEncoded
    @POST("/login")
    Call<Token> signIn(@Field("email") String email, @Field("password") String password);

    @GET("/user/api_token")
    Call<Token> apiToken(@Query(QUERY_KEY_ACCESS_TOKEN) String accessToken);

    @PATCH("/user/api_token")
    Call<Token> refreshApiToken(@Query(QUERY_KEY_ACCESS_TOKEN) String accessToken);

    // User
    @GET("/user")
    Call<User> user(@Query(QUERY_KEY_ACCESS_TOKEN) String accessToken);

    // Apps
    @GET("/apps")
    Call<List<App>> apps(@Query(QUERY_KEY_ACCESS_TOKEN) String accessToken);

    @GET("/apps/latest/{appId}")
    Call<AppInstallInfo> appInstallInfo(@Path("appId") String appId, @Query(QUERY_KEY_API_TOKEN) String apiToken);

    // Notifications

    String NOTIFICATON_TYPE_SYS = "sys";
    String NOTIFICATION_TYPE_RELEASE = "release"; // TODO Multi pages when there are too many notifications

    @GET("/notifications")
    Call<MultiPageResponse<Message>> notifications(
            @Query(QUERY_KEY_ACCESS_TOKEN) String accessToken,
            @Query("type") String type
    );

    @GET("/notifications?type=sys")
    Call<MultiPageResponse<Message>> systemNotifications(
            @Query(QUERY_KEY_ACCESS_TOKEN) String accessToken
    );
}
