package io.github.ryanhoo.firFlight.data.source.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import io.github.ryanhoo.firFlight.data.source.local.db.tables.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 5/31/16
 * Time: 1:45 PM
 * Desc: DatabaseHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    public static final int DATABASE_VERSION = 27;
    public static final String DATABASE_NAME = "fir-flight.db";

    List<Class<? extends BaseTable>> mRegisteredTables;

    /**
     * Register tables here
     */
    private void registerTables() {
        mRegisteredTables = new ArrayList<>();
        mRegisteredTables.add(AppTable.class);
        // mRegisteredTables.add(ReleaseTable.class);
        mRegisteredTables.add(MessageTable.class);
        mRegisteredTables.add(UserTable.class);
        mRegisteredTables.add(TokenTable.class);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        registerTables();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            createTables(db);
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onDelete(db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    protected void onDelete(SQLiteDatabase db) {
        try {
            deleteTables(db);
        } catch (Exception e) {
            Log.e(TAG, "onDelete: ", e);
        }
    }

    public void clearDatabase() {
        if (mRegisteredTables == null) return;

        onDelete(getWritableDatabase());
        onCreate(getWritableDatabase());
    }

    // Tables creation & deletion

    private void createTables(SQLiteDatabase db) throws IllegalAccessException, InstantiationException {
        if (mRegisteredTables == null) return;
        for (Class<? extends BaseTable> table : mRegisteredTables) {
            BaseTable tableInstance = table.newInstance();
            db.execSQL(tableInstance.createTableSql());
        }
    }

    private void deleteTables(SQLiteDatabase db) throws IllegalAccessException, InstantiationException {
        if (mRegisteredTables == null) return;
        for (Class<? extends BaseTable> table : mRegisteredTables) {
            BaseTable tableInstance = table.newInstance();
            db.execSQL(tableInstance.deleteTableSql());
        }
    }
}
