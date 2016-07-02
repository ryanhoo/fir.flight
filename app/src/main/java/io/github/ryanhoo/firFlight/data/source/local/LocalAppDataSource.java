package io.github.ryanhoo.firFlight.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.source.AppContract;
import io.github.ryanhoo.firFlight.data.source.local.db.tables.AppTable;
import rx.Observable;
import rx.functions.Func1;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/26/16
 * Time: 12:25 AM
 * Desc: LocalAppDataSource
 */
public class LocalAppDataSource extends AbstractLocalDataSource<AppTable> implements AppContract.Local {

    public LocalAppDataSource(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected AppTable instantiateTable() {
        return new AppTable();
    }

    @Override
    public Observable<List<App>> apps() {
        return mDatabaseHelper.createQuery(AppTable.TABLE_NAME, AppTable.QUERY_ALL_APPS)
                .mapToList(new Func1<Cursor, App>() {
                    @Override
                    public App call(Cursor cursor) {
                        return mTable.parseCursor(cursor);
                    }
                });
    }

    @Override
    public boolean save(App app) {
        mDatabaseHelper.insert(AppTable.TABLE_NAME, mTable.toContentValues(app), SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    @Override
    public int save(List<App> apps) {
        for (App app : apps) {
            save(app);
        }
        return 0;
    }

    @Override
    public boolean delete(App app) {
        mDatabaseHelper.delete(AppTable.TABLE_NAME, AppTable.WHERE_ID_EQUALS, app.getId());
        return true;
    }

    @Override
    public int delete(List<App> apps) {
        for (App app : apps) {
            delete(app);
        }
        return 0;
    }

    @Override
    public int deleteAll() {
        mDatabaseHelper.delete(AppTable.TABLE_NAME, null);
        return 0;
    }
}
