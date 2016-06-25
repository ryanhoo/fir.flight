package io.github.ryanhoo.firFlight.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.ryanhoo.firFlight.data.model.IMessageContent;
import io.github.ryanhoo.firFlight.network.gson.DateDeserializer;
import io.github.ryanhoo.firFlight.network.gson.MessageContentDeserializer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.internal.util.RxThreadFactory;

import java.util.Date;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/15/16
 * Time: 10:58 PM
 * Desc: RetrofitClient
 */
public class RetrofitClient {

    public static Retrofit defaultInstance() {
        return new Retrofit.Builder()
                .client(defaultOkHttpClient())
                .baseUrl(ServerConfig.API_HOST)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(defaultGson()))
                .build();
    }

    public static OkHttpClient defaultOkHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new SessionInterceptor())
                .build();
    }

    public static Gson defaultGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(IMessageContent.class, new MessageContentDeserializer())
                .create();
    }
}
