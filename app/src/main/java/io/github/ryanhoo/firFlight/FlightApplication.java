package io.github.ryanhoo.firFlight;

import android.app.Application;
import android.support.annotation.NonNull;
import io.github.ryanhoo.firFlight.analytics.FlightAnalytics;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/15/16
 * Time: 5:47 PM
 * Desc: FlightApplication
 */
public class FlightApplication extends Application {

    private static FlightApplication sInstance;

    @NonNull
    public static FlightApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        // Crash report, data analysis...
        FlightAnalytics.init(this);
    }
}
