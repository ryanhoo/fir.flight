package io.github.ryanhoo.firFlight.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.source.TokenContract;
import io.github.ryanhoo.firFlight.data.source.local.db.tables.TokenTable;

import static io.github.ryanhoo.firFlight.data.source.local.db.tables.TokenTable.*;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/3/16
 * Time: 9:39 PM
 * Desc: LocalTokenDataSource
 */
public class LocalTokenDataSource extends AbstractLocalDataSource<TokenTable> implements TokenContract.Local {

    public LocalTokenDataSource(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected TokenTable instantiateTable() {
        return new TokenTable();
    }

    @Override
    public Token token() {
        Cursor cursor = mDatabaseHelper.query(QUERY_TOKEN);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return mTable.parseCursor(cursor);
        }
        return null;
    }

    @Override
    public boolean save(Token token) {
        return mDatabaseHelper.insert(TABLE_NAME, mTable.toContentValues(token), SQLiteDatabase.CONFLICT_REPLACE) == 1;
    }

    @Override
    public boolean delete(Token token) {
        return mDatabaseHelper.delete(TABLE_NAME, WHERE_ACCESS_TOKEN_EQUALS, token.getAccessToken()) == 1;
    }

    @Override
    public boolean deleteAll() {
        return mDatabaseHelper.delete(TABLE_NAME, null) == 1;
    }
}
