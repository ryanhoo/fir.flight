package io.github.ryanhoo.firFlight.data.source.local.db.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;
import io.github.ryanhoo.firFlight.data.model.IMessageContent;
import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.util.ObjectUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/1/16
 * Time: 1:18 PM
 * Desc: MessageTable
 */
public final class MessageTable implements BaseColumns, BaseTable<Message> {

    private static final String TAG = "MessageTable";

    // Table Name
    public static final String TABLE_NAME = "message";

    // Columns
    public static final String COLUMN_ID = _ID; // "_ID"
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_IS_READ = "is_read";
    public static final String COLUMN_TEMPLATE = "template";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_CONTENT = "content";

    // Create & Delete
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                " ( " +
                    COLUMN_ID + " TEXT PRIMARY KEY UNIQUE, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_IS_READ + " INTEGER DEFAULT 0, " +
                    COLUMN_TEMPLATE + " TEXT, " +
                    COLUMN_CREATED_AT + " INTEGER, " +
                    COLUMN_CONTENT + " TEXT" +
                " );";

    public static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public static final String QUERY_ALL_SYSTEM_MESSAGES =
            "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TYPE + "=?";

    public static final String WHERE_ID_EQUALS = COLUMN_ID + "=?";

    @Override
    public String createTableSql() {
        return CREATE_TABLE;
    }

    @Override
    public String deleteTableSql() {
        return DELETE_TABLE;
    }

    public ContentValues toContentValues(Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, message.getId());
        contentValues.put(COLUMN_TYPE, message.getType());
        contentValues.put(COLUMN_IS_READ, message.isRead() ? 1 : 0);
        contentValues.put(COLUMN_TEMPLATE, message.getTemplate());
        contentValues.put(COLUMN_CREATED_AT, message.getCreatedAt() == null ? -1 : message.getCreatedAt().getTime());
        if (message.getContent() != null) {
            try {
                contentValues.put(COLUMN_CONTENT, ObjectUtils.objectToByte(message.getContent()));
            } catch (IOException e) {
                Log.e(TAG, "When saving message content: " + message.getContent(), e);
            }
        }
        return contentValues;
    }

    public Message parseCursor(Cursor c) {
        Message message = new Message();
        message.setId(c.getString(c.getColumnIndexOrThrow(COLUMN_ID)));
        message.setType(c.getString(c.getColumnIndexOrThrow(COLUMN_TYPE)));
        message.setRead(c.getInt(c.getColumnIndexOrThrow(COLUMN_IS_READ)) == 1);
        message.setTemplate(c.getString(c.getColumnIndexOrThrow(COLUMN_TEMPLATE)));
        long createdAt = c.getLong(c.getColumnIndexOrThrow(COLUMN_CREATED_AT));
        message.setCreatedAt(createdAt == -1 ? null : new Date(createdAt));
        byte[] bytes = c.getBlob(c.getColumnIndexOrThrow(COLUMN_CONTENT));
        if (bytes != null) {
            try {
                message.setContent((IMessageContent) ObjectUtils.byteToObject(bytes));
            } catch (IOException | ClassNotFoundException e) {
                Log.e(TAG, "When parsing message content: " + new String(bytes), e);
            }
        }
        return message;
    }
}
