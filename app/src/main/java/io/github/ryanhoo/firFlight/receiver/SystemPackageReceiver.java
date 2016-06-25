package io.github.ryanhoo.firFlight.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/20/16
 * Time: 11:05 PM
 * Desc: SystemPackageReceiver
 */
public class SystemPackageReceiver extends BroadcastReceiver {

    private static final String TAG = "SystemPackageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());
    }
}
