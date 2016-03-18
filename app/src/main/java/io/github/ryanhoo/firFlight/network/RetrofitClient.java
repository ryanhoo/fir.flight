package io.github.ryanhoo.firFlight.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/15/16
 * Time: 10:58 PM
 * Desc: RetrofitClient
 */
public class RetrofitClient {

    public static Retrofit defaultInstance() {
        return new Retrofit.Builder()
                .baseUrl(ServerConfig.API_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
