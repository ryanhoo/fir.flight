package io.github.ryanhoo.firFlight.data.source.local.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import io.github.ryanhoo.firFlight.data.model.Message;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/1/16
 * Time: 1:18 PM
 * Desc: MessageTable
 */
public final class MessageTable implements BaseColumns {

    // Table Name
    public static final String TABLE_NAME = "message";

    // Columns
    public static final String COLUMN_ID = "message_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_IS_READ = "is_read";
    public static final String COLUMN_TEMPLATE = "template";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_CONTENT = "content";

    // Create & Delete
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                " ( " +
                    _ID + " INTEGER, " +
                    COLUMN_ID + " TEXT PRIMARY KEY UNIQUE, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_IS_READ + " INTEGER DEFAULT 0, " +
                    COLUMN_TEMPLATE + " TEXT, " +
                    COLUMN_CREATED_AT + " INTEGER, " +
                    COLUMN_CONTENT + " TEXT NOT NULL" +
                " );";

    public static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public static ContentValues toContentValues(Message message) {
        return null;
    }

    public static Message parseCursor(Cursor c) {
        return null;
    }
}
