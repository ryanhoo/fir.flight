package io.github.ryanhoo.firFlight.network.gson;

import android.util.Log;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 9:35 PM
 * Desc: DateDeserializer
 */
public class DateDeserializer implements JsonDeserializer<Date> {

    private static final String TAG = "DateDeserializer";

    // 1455859798.276
    static final String DATE_WITH_MILLIS_3 = "\\d{10}\\.\\d{3}";

    // 1453271675.02
    static final String DATE_WITH_MILLIS_2 = "\\d{10}\\.\\d{2}";

    // 1455859798
    static final String DATE_WITH_SECONDS = "\\d{10}";

    @Override
    public Date deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            String dateString = jsonElement.getAsString();
            if (dateString.matches(DATE_WITH_MILLIS_3)) {
                long timestamp = Long.parseLong(dateString.replace(".", ""));
                return new Date(timestamp);
            } else if (dateString.matches(DATE_WITH_SECONDS)) {
                long timestamp = Long.parseLong(dateString);
                return new Date(timestamp * 1000);
            } else if (dateString.matches(DATE_WITH_MILLIS_2)) {
                long timestamp = Long.parseLong(dateString.replace(".", ""));
                return new Date(timestamp * 10);
            } else {
                Log.e(TAG, "Unable to parse date " + jsonElement);
            }
        } catch (Exception e) {
            Log.e(TAG, "deserialize: " + jsonElement, e);
        }
        return null;
    }
}
