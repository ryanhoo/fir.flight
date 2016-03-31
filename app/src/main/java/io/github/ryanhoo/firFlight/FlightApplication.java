package io.github.ryanhoo.firFlight;

import android.app.Application;
import im.fir.sdk.FIR;
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
        // BugHD
        FIR.init(this);

        // User Session
        UserSession.init(this);
    }
}
