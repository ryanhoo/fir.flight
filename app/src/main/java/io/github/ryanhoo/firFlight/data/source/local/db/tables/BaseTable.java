package io.github.ryanhoo.firFlight.data.source.local.db.tables;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/1/16
 * Time: 1:02 PM
 * Desc: BaseTable
 */
public interface BaseTable<T> {

    String createTableSql();

    String deleteTableSql();

    ContentValues toContentValues(T t);

    T parseCursor(Cursor cursor);
}
