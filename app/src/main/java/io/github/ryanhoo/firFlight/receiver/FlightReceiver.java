package io.github.ryanhoo.firFlight.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import io.github.ryanhoo.firFlight.service.FlightService;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/23/16
 * Time: 8:43 PM
 * Desc: FlightReceiver
 */
public class FlightReceiver extends BroadcastReceiver {

    private static final String TAG = "FlightReceiver";

    public static final String ACTION_UPDATE = "io.github.ryanhoo.action.UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());
        // if (ACTION_UPDATE.equals(intent.getAction())) {
        // Start update service
        Intent serviceIntent = new Intent(context, FlightService.class);
        context.startService(serviceIntent);
        // }
    }
}
