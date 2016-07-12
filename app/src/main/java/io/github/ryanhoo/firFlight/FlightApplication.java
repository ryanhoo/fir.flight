package io.github.ryanhoo.firFlight;

import android.app.Application;
import android.support.annotation.NonNull;
import com.facebook.stetho.Stetho;
import io.github.ryanhoo.firFlight.analytics.FlightAnalytics;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
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

        // Custom fonts
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        // Facebook debug bridge
        Stetho.initializeWithDefaults(this);

        // Crash report, data analysis...
        FlightAnalytics.init(this);
    }
}
