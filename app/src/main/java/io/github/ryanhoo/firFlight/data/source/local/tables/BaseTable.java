package io.github.ryanhoo.firFlight.data.source.local.tables;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 6/1/16
 * Time: 1:02 PM
 * Desc: BaseTable
 */
public interface BaseTable<T> {

    String createTable();

    String deleteTable();

    ContentValues toContentValues(T t);

    T parseCursor(Cursor cursor);
}
