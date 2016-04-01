package io.github.ryanhoo.firFlight;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.BuildConfig;
import im.fir.sdk.FIR;
import io.fabric.sdk.android.Fabric;
import io.github.ryanhoo.firFlight.data.UserSession;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/15/16
 * Time: 5:47 PM
 * Desc: FlightApplication
 */
public class FlightApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Fabric: Crashlytics, Answers
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics(), new Answers())
                .debuggable(BuildConfig.DEBUG)
                .build();
        Fabric.with(fabric);

        // BugHD
        FIR.init(this);

        // User Session
        UserSession.init(this);
    }
}
