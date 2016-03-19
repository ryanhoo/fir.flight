package io.github.ryanhoo.firFlight.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.ryanhoo.firFlight.network.gson.DateDeserializer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Date;

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
                .addConverterFactory(GsonConverterFactory.create(defaultGson()))
                .build();
    }

    public static Gson defaultGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }
}
