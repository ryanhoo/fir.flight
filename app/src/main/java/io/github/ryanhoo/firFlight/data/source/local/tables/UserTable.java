package io.github.ryanhoo.firFlight.data.source.local.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import io.github.ryanhoo.firFlight.data.model.User;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/1/16
 * Time: 1:34 PM
 * Desc: UserTable
 */
public final class UserTable implements BaseColumns {

    // Table Name
    public static final String TABLE_NAME = "user";

    // Columns
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_GRAVATAR = "gravatar";
    public static final String COLUMN_IS_CONFIRMED = "is_confirmed";
    public static final String COLUMN_IS_DEVELOPER = "is_developer";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Create & Delete
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                " ( " +
                    _ID + " INTEGER, " +
                    COLUMN_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_UUID + " TEXT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_GRAVATAR + " TEXT, " +
                    COLUMN_IS_CONFIRMED + " INTEGER DEFAULT 0, " +
                    COLUMN_IS_DEVELOPER + " INTEGER DEFAULT 0, " +
                    COLUMN_CREATED_AT + " INTEGER" +
                " );";

    public static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public static ContentValues toContentValues(User user) {
        return null;
    }

    public static User parseCursor(Cursor c) {
        return null;
    }
}
