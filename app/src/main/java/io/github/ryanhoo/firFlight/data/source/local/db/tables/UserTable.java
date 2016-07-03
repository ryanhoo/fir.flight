package io.github.ryanhoo.firFlight.data.source.local.db.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import io.github.ryanhoo.firFlight.data.model.User;

import java.util.Date;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/1/16
 * Time: 1:34 PM
 * Desc: UserTable
 */
public final class UserTable implements BaseColumns, BaseTable<User> {

    // Table Name
    public static final String TABLE_NAME = "user";

    // Columns
    public static final String COLUMN_ID = _ID; // "_id"
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

    public static final String QUERY_USER =
            "SELECT * FROM " + TABLE_NAME + ";";

    public static final String WHERE_ID_EQUALS = COLUMN_ID + "=?";

    @Override
    public String createTableSql() {
        return CREATE_TABLE;
    }

    @Override
    public String deleteTableSql() {
        return DELETE_TABLE;
    }

    public ContentValues toContentValues(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, user.getId());
        contentValues.put(COLUMN_UUID, user.getUuid());
        contentValues.put(COLUMN_NAME, user.getName());
        contentValues.put(COLUMN_EMAIL, user.getEmail());
        contentValues.put(COLUMN_GRAVATAR, user.getGravatar());
        contentValues.put(COLUMN_IS_CONFIRMED, user.isConfirmed() ? 1 : 0);
        contentValues.put(COLUMN_IS_DEVELOPER, user.isDeveloper() ? 1 : 0);
        contentValues.put(COLUMN_CREATED_AT, user.getCreatedAt() == null ? -1 : user.getCreatedAt().getTime());
        return contentValues;
    }

    public User parseCursor(Cursor c) {
        User user = new User();
        user.setId(c.getString(c.getColumnIndexOrThrow(COLUMN_ID)));
        user.setUuid(c.getString(c.getColumnIndexOrThrow(COLUMN_UUID)));
        user.setName(c.getString(c.getColumnIndexOrThrow(COLUMN_NAME)));
        user.setEmail(c.getString(c.getColumnIndexOrThrow(COLUMN_EMAIL)));
        user.setGravatar(c.getString(c.getColumnIndexOrThrow(COLUMN_GRAVATAR)));
        user.setConfirmed(c.getInt(c.getColumnIndexOrThrow(COLUMN_IS_CONFIRMED)) == 1);
        user.setDeveloper(c.getInt(c.getColumnIndexOrThrow(COLUMN_IS_DEVELOPER)) == 1);
        long createdAt = c.getLong(c.getColumnIndexOrThrow(COLUMN_CREATED_AT));
        user.setCreatedAt(createdAt == -1 ? null : new Date(createdAt));
        return user;
    }
}
