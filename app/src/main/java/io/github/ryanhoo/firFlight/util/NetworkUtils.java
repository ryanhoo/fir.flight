package io.github.ryanhoo.firFlight.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/5/16
 * Time: 11:36 PM
 * Desc: NetworkUtils
 */
public class NetworkUtils {

    public static boolean isNetworkAvailable(@Nullable final Context context) {
        if (context == null) return false;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
    }
}
