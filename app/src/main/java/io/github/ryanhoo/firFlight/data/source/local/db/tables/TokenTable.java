package io.github.ryanhoo.firFlight.data.source.local.db.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import io.github.ryanhoo.firFlight.data.model.Token;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/1/16
 * Time: 1:34 PM
 * Desc: TokenTable
 */
public final class TokenTable implements BaseColumns, BaseTable<Token> {

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

    public static final String QUERY_TOKEN = "SELECT * FROM " + TABLE_NAME + ";";

    public static final String WHERE_ACCESS_TOKEN_EQUALS =
            "WHERE " + COLUMN_ACCESS_TOKEN + "=?";

    @Override
    public String createTableSql() {
        return CREATE_TABLE;
    }

    @Override
    public String deleteTableSql() {
        return DELETE_TABLE;
    }

    public ContentValues toContentValues(Token token) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ACCESS_TOKEN, token.getAccessToken());
        contentValues.put(COLUMN_API_TOKEN, token.getApiToken());
        return contentValues;
    }

    public Token parseCursor(Cursor c) {
        Token token = new Token();
        token.setAccessToken(c.getString(c.getColumnIndexOrThrow(COLUMN_ACCESS_TOKEN)));
        token.setApiToken(c.getString(c.getColumnIndexOrThrow(COLUMN_API_TOKEN)));
        return token;
    }

}
