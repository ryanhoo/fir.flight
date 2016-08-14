package io.github.ryanhoo.firFlight.analytics;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.Fabric;
import io.github.ryanhoo.firFlight.BuildConfig;
import io.github.ryanhoo.firFlight.account.UserSession;
import io.github.ryanhoo.firFlight.data.model.User;

import static io.github.ryanhoo.firFlight.analytics.FlightEvent.*;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 4/1/16
 * Time: 6:11 PM
 * Desc: FlightAnalytics
 * - Crash reports
 * - Event analysis
 */
public class FlightAnalytics {

    private static final String TAG = "FlightAnalytics";

    public static boolean isEnabled() {
        return BuildConfig.FABRIC_ENABLED;
    }

    /**
     * <pre>
     * Support
     *
     * Crash reports
     * - BugHD(fir.im)
     * - Crashlytics(fabric.io)
     *
     * Analyse
     * - Answers(fabric.io)
     * </pre>
     */
    public static void init(Application application) {
        if (!isEnabled()) return;

        Context appContext = application.getApplicationContext();
        // Fabric: Crashlytics, Answers
        final Fabric fabric = new Fabric.Builder(appContext)
                .kits(new Crashlytics(), new Answers())
                .debuggable(BuildConfig.DEBUG)
                .build();
        Fabric.with(fabric);

        configFlavor();
        configUserSession(UserSession.getInstance());

        // Event: Open App
        FlightEvent openAppEvent = new FlightEvent(EVENT_OPEN_APP);
        if (UserSession.getInstance().isSignedIn()) {
            User user = UserSession.getInstance().getUser();
            if (user != null) {
                openAppEvent.putCustomAttribute(KEY_ID, user.getId());
                openAppEvent.putCustomAttribute(KEY_NAME, user.getName());
                openAppEvent.putCustomAttribute(KEY_EMAIL, user.getEmail());
            }
        }
        onEvent(openAppEvent);
    }

    private static void configFlavor() {
        if (!isEnabled()) return;

        Crashlytics.setString(KEY_FLAVOR, BuildConfig.FLAVOR);
    }

    public static void configUserSession(UserSession userSession) {
        if (!isEnabled()) return;

        // TODO tokens are sensitive information, better get permission from users
        // Default shouldn't post tokens on server, but users can choose to open it in Settings
        if (userSession.isSignedIn()) {
            Log.d(TAG, "Config UserSession");
            /*
            Token token = UserSession.getInstance().getToken();
            if (token != null) {
                Crashlytics.setString("accessToken", token.getAccessToken());
                Crashlytics.setString("apiToken", token.getApiToken());
            }
            */
            User user = userSession.getUser();
            if (user != null) {
                Crashlytics.setString(KEY_ID, user.getId());
                Crashlytics.setString(KEY_NAME, user.getName());
                Crashlytics.setString(KEY_EMAIL, user.getEmail());
            }
        } else {
            Log.e(TAG, "Config UserSession failed, session is null");
        }
    }

    public static void onEvent(FlightEvent event) {
        if (!isEnabled()) return;

        Answers.getInstance().logCustom(event);
    }
}
