package io.github.ryanhoo.firFlight.data.source.local.db.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import io.github.ryanhoo.firFlight.data.model.Release;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/1/16
 * Time: 1:14 PM
 * Desc: ReleaseTable
 */
public final class ReleaseTable implements BaseColumns, BaseTable<Release> {

    // Table Name
    public static final String TABLE_NAME = "release";

    // Columns
    public static final String COLUMN_ID = _ID; // "_id"
    public static final String COLUMN_VERSION = "version";
    public static final String COLUMN_BUILD = "build";
    public static final String COLUMN_RELEASE_TYPE = "release_type";
    public static final String COLUMN_CHANGELOG = "changelog";
    public static final String COLUMN_DISTRIBUTION_NAME = "distribution_name";
    public static final String COLUMN_SUPPORTED_PLATFORM = "supported_platform";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Create & Delete
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                " ( " +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_VERSION + " TEXT, " +
                    COLUMN_BUILD + " TEXT, " +
                    COLUMN_RELEASE_TYPE + " TEXT, " +
                    COLUMN_CHANGELOG + " TEXT, " +
                    COLUMN_DISTRIBUTION_NAME + " TEXT, " +
                    COLUMN_SUPPORTED_PLATFORM + " TEXT, " +
                    COLUMN_CREATED_AT + " INTEGER" +
                " );";

    public static final String DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    @Override
    public String createTableSql() {
        return CREATE_TABLE;
    }

    @Override
    public String deleteTableSql() {
        return DELETE_TABLE;
    }

    @Override
    public ContentValues toContentValues(Release release) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VERSION, release.getVersion());
        contentValues.put(COLUMN_BUILD, release.getBuild());
        contentValues.put(COLUMN_CHANGELOG, release.getChangelog());
        contentValues.put(COLUMN_RELEASE_TYPE, release.getReleaseType());
        contentValues.put(COLUMN_DISTRIBUTION_NAME, release.getDistributionName());
        contentValues.put(COLUMN_SUPPORTED_PLATFORM, release.getSupportedPlatform());
        contentValues.put(COLUMN_CREATED_AT, release.getCreatedAt());
        return contentValues;
    }

    @Override
    public Release parseCursor(Cursor c) {
        Release release = new Release();
        release.setId(c.getLong(c.getColumnIndexOrThrow(COLUMN_ID)));
        release.setVersion(c.getString(c.getColumnIndexOrThrow(COLUMN_VERSION)));
        release.setBuild(c.getString(c.getColumnIndexOrThrow(COLUMN_BUILD)));
        release.setReleaseType(c.getString(c.getColumnIndexOrThrow(COLUMN_RELEASE_TYPE)));
        release.setChangelog(c.getString(c.getColumnIndexOrThrow(COLUMN_CHANGELOG)));
        release.setDistributionName(c.getString(c.getColumnIndexOrThrow(COLUMN_DISTRIBUTION_NAME)));
        release.setSupportedPlatform(c.getString(c.getColumnIndexOrThrow(COLUMN_SUPPORTED_PLATFORM)));
        release.setCreatedAt(c.getLong(c.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
        return release;
    }
}
