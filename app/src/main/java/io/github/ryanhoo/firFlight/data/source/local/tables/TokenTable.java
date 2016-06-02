package io.github.ryanhoo.firFlight.data.source.local.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import io.github.ryanhoo.firFlight.data.model.Token;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 6/1/16
 * Time: 1:34 PM
 * Desc: TokenTable
 */
public final class TokenTable implements BaseColumns {

    // Table Name
    public static final String TABLE_NAME = "token";

    // Columns
    public static final String COLUMN_ACCESS_TOKEN = "access_token";
    public static final String COLUMN_API_TOKEN = "api_token";

    // Create & Delete
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                " ( " +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ACCESS_TOKEN + " TEXT, " +
                    COLUMN_API_TOKEN + " TEXT" +
                " );";

    public static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public static ContentValues toContentValues(Token token) {
        return null;
    }

    public static Token parseCursor(Cursor c) {
        return null;
    }

}
