package io.github.ryanhoo.firFlight.network.gson;

import android.util.Log;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.github.ryanhoo.firFlight.data.model.IMessageContent;
import io.github.ryanhoo.firFlight.data.model.impl.ReleaseMessageContent;
import io.github.ryanhoo.firFlight.data.model.impl.SystemMessageContent;

import java.lang.reflect.Type;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 11:53 PM
 * Desc: MessageContentDeserializer
 */
public class MessageContentDeserializer implements JsonDeserializer<IMessageContent> {

    private static final String TAG = "MessageDeserializer";

    @Override
    public IMessageContent deserialize(JsonElement jsonElement, Type type,
                                       JsonDeserializationContext gson) throws JsonParseException {
        try {
            if (jsonElement.getAsJsonObject().has("link")) {
                // System
                return gson.deserialize(jsonElement, SystemMessageContent.class);
            } else if (jsonElement.getAsJsonObject().has("release")) {
                // Release
                return gson.deserialize(jsonElement, ReleaseMessageContent.class);
            }
        } catch (Exception e) {
            Log.e(TAG, "Deserialize failed " + jsonElement, e);
        }
        return null;
    }

}
