package io.github.ryanhoo.firFlight.network;

import android.content.Context;
import android.util.Log;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.RxBus;
import io.github.ryanhoo.firFlight.event.SignOutEvent;
import io.github.ryanhoo.firFlight.ui.common.alert.FlightToast;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/11/16
 * Time: 12:36 AM
 * Desc: A default subscriber, the purpose of creating this is meant to provide a default solution
 * to handle the network request's errors.
 * <p/>
 * It holds a context as a weak reference. But the context is nullable, if it is null then all of the error
 * messages will be logged into logcat console, otherwise it will toast all the error messages to the user.
 * <p/>
 * Be careful when a context is set, toast must be shown on android's main thread. Remember to call
 * observable.observeOn(AndroidSchedulers.mainThread()) if you want to show toast.
 */
public class NetworkSubscriber<T> extends Subscriber<T> {

    private static final String TAG = "NetworkSubscriber";
    public static final int HTTP_CODE_UNAUTHORIZED = 401;

    private WeakReference<Context> mWeakContext;

    public NetworkSubscriber(Context context) {
        mWeakContext = new WeakReference<>(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onCompleted() {
        onUnsubscribe();
    }

    @Override
    public void onError(Throwable e) {
        Context context = mWeakContext.get();
        String message = e.getMessage();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            // Access Token has expired, time to sign out
            if (httpException.code() == HTTP_CODE_UNAUTHORIZED) {
                RxBus.getInstance().post(new SignOutEvent());
                if (context != null) {
                    message = context.getString(R.string.ff_network_error_session_expired);
                } else {
                    message = httpException.message();
                }
            } else {
                message = httpException.message();
            }
        } else if (e instanceof SocketTimeoutException) {
            if (context != null) {
                message = context.getString(R.string.ff_network_error_timeout);
            }
        }
        if (context != null) {
            new FlightToast.Builder(context)
                    .message(message)
                    .show();
        } else {
            Log.e(TAG, "" + message, e);
        }
        onUnsubscribe();
    }

    /**
     * Gets called after #onCompleted or #onError, you can handle some common operations in here
     * if you don't mind the result is a success or failure.
     * <p/>
     * - It might be a little confusing that though it's called onUnsubscribe, but it's getting called
     * before subclass's onComplete or onError method invoke.
     * - If this is implemented by subclass, don't forget to call super.onCompleted or super.onError
     * in their related methods.
     */
    public void onUnsubscribe() {
        // Things you wanna do after onCompleted or onError
    }
}
