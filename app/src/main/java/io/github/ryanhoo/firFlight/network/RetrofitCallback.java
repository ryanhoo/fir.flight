package io.github.ryanhoo.firFlight.network;

import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/16/16
 * Time: 8:03 AM
 * Desc: RetrofitCallback
 */
public abstract class RetrofitCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.code() < 400) {
            onSuccess(call, response, response.body());
        } else if (response.code() < 500) {
            NetworkError error = new Gson().fromJson(response.errorBody().charStream(), NetworkError.class);
            onFailure(call, error);
        } else {
            NetworkError error = new NetworkError(response.code(), response.message());
            onFailure(call, error);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            // Unable to resolve host "api.fir.ims": No address associated with hostname
        } else if (throwable instanceof SocketTimeoutException) {
            // Socket time out
        }
        NetworkError error = new NetworkError(NetworkError.ERROR_CODE_UNKNOWN, throwable.getLocalizedMessage());
        onFailure(call, error);
    }

    public abstract void onSuccess(Call<T> call, Response httpResponse, T t);

    public abstract void onFailure(Call<T> call, NetworkError error);
}
