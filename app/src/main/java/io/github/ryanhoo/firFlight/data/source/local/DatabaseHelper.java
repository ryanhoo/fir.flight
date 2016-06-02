package io.github.ryanhoo.firFlight.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import io.github.ryanhoo.firFlight.data.source.local.tables.*;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 5/31/16
 * Time: 1:45 PM
 * Desc: DatabaseHelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    public static final int DATABASE_VERSION = 20;
    public static final String DATABASE_NAME = "fir-flight.db";

    List<Class<? extends BaseTable>> mRegisteredTables;

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
        db.execSQL(UserTable.CREATE_TABLE);
        db.execSQL(TokenTable.CREATE_TABLE);
        db.execSQL(AppTable.CREATE_TABLE);
        db.execSQL(ReleaseTable.CREATE_TABLE);
        db.execSQL(MessageTable.CREATE_TABLE);
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
        db.execSQL(UserTable.DELETE_TABLE);
        db.execSQL(TokenTable.DELETE_TABLE);
        db.execSQL(AppTable.DELETE_TABLE);
        db.execSQL(ReleaseTable.DELETE_TABLE);
        db.execSQL(MessageTable.DELETE_TABLE);
    }

    // Tables

    private void registerTables() {
        //        mRegisteredTables = new ArrayList<>();
        //        mRegisteredTables.add(AppTable.class);
        //        mRegisteredTables.add(ReleaseTable.class);
        //        mRegisteredTables.add(AppReleaseTable.class);
        //        mRegisteredTables.add(MessageTable.class);
        //        mRegisteredTables.add(UserTable.class);
        //        mRegisteredTables.add(TokenTable.class);
    }

    private void createTables(SQLiteDatabase db) throws IllegalAccessException, InstantiationException {
        if (mRegisteredTables == null) return;
        for (Class<? extends BaseTable> table : mRegisteredTables) {
            BaseTable tableInstance = table.newInstance();
            db.execSQL(tableInstance.createTable());
        }
    }

    private void deleteTables(SQLiteDatabase db) throws IllegalAccessException, InstantiationException {
        if (mRegisteredTables == null) return;
        for (Class<? extends BaseTable> table : mRegisteredTables) {
            BaseTable tableInstance = table.newInstance();
            db.execSQL(tableInstance.deleteTable());
        }
    }
}
