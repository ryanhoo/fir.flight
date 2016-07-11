package io.github.ryanhoo.firFlight.util;

import android.content.Context;
import android.util.Log;
import io.github.ryanhoo.firFlight.data.source.local.db.DatabaseHelper;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/3/16
 * Time: 10:23 PM
 * Desc: DbUtils
 */
public class DbUtils {

    private static final String TAG = "DbUtils";

    public static void clearDataBase(Context context) {
        boolean result = context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
        Log.i(TAG, "clearDataBase: " + (result ? "succeeded" : "failed"));
    }
}
