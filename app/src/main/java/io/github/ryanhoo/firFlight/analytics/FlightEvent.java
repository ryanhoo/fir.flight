package io.github.ryanhoo.firFlight.analytics;

import com.crashlytics.android.answers.CustomEvent;
import io.github.ryanhoo.firFlight.BuildConfig;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 4/1/16
 * Time: 6:09 PM
 * Desc: FlightEvent
 */
public class FlightEvent extends CustomEvent {

    // Events
    public static final String EVENT_SIGN_IN = "Sign In";
    public static final String EVENT_SIGN_OUT = "Sign Out";
    public static final String EVENT_OPEN_APP = "Open App";
    // On add new account
    public static final String EVENT_ACCOUNT = "Account";
    // On request & refresh api token failed
    public static final String EVENT_API_TOKEN = "Api Token";

    // Keys
    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_FLAVOR = "flavor";
    public static final String KEY_SUCCESS = "success";

    public FlightEvent(String eventName) {
        super(eventName);
        putCustomAttribute(KEY_FLAVOR, BuildConfig.FLAVOR);
    }

    @Override
    public FlightEvent putCustomAttribute(String key, String value) {
        super.putCustomAttribute(key, value);
        return this;
    }

    @Override
    public FlightEvent putCustomAttribute(String key, Number value) {
        super.putCustomAttribute(key, value);
        return this;
    }

    public FlightEvent putSuccess(boolean success) {
        super.putCustomAttribute(KEY_SUCCESS, Boolean.toString(success));
        return this;
    }
}
