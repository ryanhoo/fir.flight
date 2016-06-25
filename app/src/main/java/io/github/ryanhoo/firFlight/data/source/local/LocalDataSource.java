package io.github.ryanhoo.firFlight.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.Release;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.data.source.DataSourceContract;
import io.github.ryanhoo.firFlight.data.source.local.tables.AppTable;
import io.github.ryanhoo.firFlight.data.source.local.tables.ReleaseTable;
import io.github.ryanhoo.firFlight.data.source.local.tables.UserTable;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 5/31/16
 * Time: 8:43 PM
 * Desc: LocalDataSource
 */
public class LocalDataSource implements DataSourceContract {

    private static LocalDataSource sInstance;
    private final BriteDatabase mDatabaseHelper;

    private LocalDataSource(@NonNull Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(databaseHelper, Schedulers.io());
    }

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new LocalDataSource(context);
        }
        return sInstance;
    }

    @Override
    public Observable<User> user() {
        final String sqlQuery = String.format("SELECT * FROM %s", UserTable.TABLE_NAME);
        return mDatabaseHelper.createQuery(UserTable.TABLE_NAME, sqlQuery)
                .mapToOne(new Func1<Cursor, User>() {
                    @Override
                    public User call(Cursor c) {
                        return new UserTable().parseCursor(c);
                    }
                });
    }

    @Override
    public Observable<List<App>> apps() {
        final String appSqlQuery = String.format("SELECT * FROM %s", AppTable.TABLE_NAME);
        final String releaseSqlQuery = String.format("SELECT * FROM %s WHERE %s=?",
                ReleaseTable.TABLE_NAME, ReleaseTable._ID);
        return mDatabaseHelper.createQuery(AppTable.TABLE_NAME, appSqlQuery)
                .mapToList(new Func1<Cursor, App>() {
                    @Override
                    public App call(Cursor ac) {
                        App app = new AppTable().parseCursor(ac);
                        long masterReleaseId = ac.getLong(ac.getColumnIndexOrThrow(AppTable.COLUMN_RELEASE_ID));
                        Cursor rc = mDatabaseHelper.query(releaseSqlQuery, String.valueOf(masterReleaseId));
                        rc.moveToFirst();
                        app.setMasterRelease(new ReleaseTable().parseCursor(rc));
                        return app;
                    }
                });
    }

    public long saveApp(App app) {
        if (isAppExists(app)) {
            deleteApp(app);
        }
        ContentValues contentValues = new AppTable().toContentValues(app);
        long masterReleaseId = saveRelease(app.getMasterRelease());
        contentValues.put(AppTable.COLUMN_RELEASE_ID, masterReleaseId);
        return mDatabaseHelper.insert(AppTable.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long saveRelease(Release release) {
        ContentValues contentValues = new ReleaseTable().toContentValues(release);
        return mDatabaseHelper.insert(ReleaseTable.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public boolean isAppExists(App app) {
        final String queryApp = String.format("SELECT %s FROM %s WHERE %s=?",
                AppTable.COLUMN_ID, AppTable.TABLE_NAME, AppTable.COLUMN_ID);
        Cursor cursor = mDatabaseHelper.query(queryApp, app.getId());
        return cursor.getCount() == 1;
    }

    public void deleteApp(App app) {
        mDatabaseHelper.execute(AppTable.DELETE_APP_TRIGGER);
        mDatabaseHelper.delete(AppTable.TABLE_NAME, AppTable.COLUMN_ID + "=?", app.getId());
    }
}
