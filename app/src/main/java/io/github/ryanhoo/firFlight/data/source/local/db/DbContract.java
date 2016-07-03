package io.github.ryanhoo.firFlight.data.source.local.db;

import android.provider.BaseColumns;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 5/31/16
 * Time: 4:12 PM
 * Desc: DbContract
 */
public final class DbContract {

    public DbContract() {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
    }

    /* Inner class that defines the table contents */
    // Replaced by AppTable
    @Deprecated
    public static abstract class AppEntry implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "app";

        // Columns
        public static final String COLUMN_APP_ID = "app_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SHORT_URL = "short_url";
        public static final String COLUMN_BUNDLE_ID = "bundle_id";
        public static final String COLUMN_CUSTOM_MARKET_URL = "custom_market_url";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_ICON_URL = "icon_url";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_MASTER_RELEASE_ID = "master_release";

        // Create & Delete
        /* package */ static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                    " ( " +
                        _ID + " INTEGER, " +
                        COLUMN_APP_ID + " TEXT PRIMARY KEY UNIQUE, " +
                        COLUMN_USER_ID + " TEXT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_SHORT_URL + " TEXT, " +
                        COLUMN_BUNDLE_ID + " TEXT, " +
                        COLUMN_CUSTOM_MARKET_URL + " TEXT, " +
                        COLUMN_CREATED_AT + " INTEGER, " +
                        COLUMN_ICON_URL + " TEXT , " +
                        COLUMN_TYPE + " TEXT , " +
                        COLUMN_MASTER_RELEASE_ID + " INTEGER" +
                    " );";
        /* package */ static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    // Replaced by ReleaseTable
    @Deprecated
    public static abstract class ReleaseEntry implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "release";

        // Columns
        public static final String COLUMN_VERSION = "version";
        public static final String COLUMN_BUILD = "build";
        public static final String COLUMN_RELEASE_TYPE = "release_type";
        public static final String COLUMN_CHANGELOG = "changelog";
        public static final String COLUMN_DISTRIBUTION_NAME = "distribution_name";
        public static final String COLUMN_SUPPORTED_PLATFORM = "supported_platform";
        public static final String COLUMN_CREATED_AT = "created_at";

        // Create & Delete
        /* package */ static final String CREATE_TABLE =
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
        /* package*/ static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    // Replaced by MessageTable
    @Deprecated
    public static abstract class MessageEntry implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "message";

        // Columns
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_IS_READ = "is_read";
        public static final String COLUMN_TEMPLATE = "template";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_CONTENT = "content";

        // Create & Delete
        /* package */ static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                    " ( " +
                        _ID + " TEXT PRIMARY KEY, " +
                        COLUMN_TYPE + " TEXT, " +
                        COLUMN_IS_READ + " INTEGER DEFAULT 0, " +
                        COLUMN_TEMPLATE + " TEXT, " +
                        COLUMN_CREATED_AT + " INTEGER, " +
                        COLUMN_CONTENT + " TEXT NOT NULL" +
                    " );";
        /* package*/ static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    // Replaced by UserTable
    @Deprecated
    public static abstract class UserEntry implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "user";

        // Columns
        public static final String COLUMN_UUID = "uuid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_GRAVATAR = "gravatar";
        public static final String COLUMN_IS_CONFIRMED = "is_confirmed";
        public static final String COLUMN_IS_DEVELOPER = "is_developer";
        public static final String COLUMN_CREATED_AT = "created_at";

        // Create & Delete
        /* package */ static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                    " ( " +
                        _ID + " TEXT PRIMARY KEY, " +
                        COLUMN_UUID + " TEXT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_EMAIL + " TEXT NOT NULL, " +
                        COLUMN_GRAVATAR + " TEXT, " +
                        COLUMN_IS_CONFIRMED + " INTEGER DEFAULT 0, " +
                        COLUMN_IS_DEVELOPER + " INTEGER DEFAULT 0, " +
                        COLUMN_CREATED_AT + " INTEGER" +
                    " );";
        /* package*/ static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    // Replaced by TokenTable
    @Deprecated
    public static abstract class TokenEntry implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "token";

        // Columns
        public static final String COLUMN_ACCESS_TOKEN = "access_token";
        public static final String COLUMN_API_TOKEN = "api_token";

        // Create & Delete
        /* package */ static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                    " ( " +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_ACCESS_TOKEN + " TEXT, " +
                        COLUMN_API_TOKEN + " TEXT" +
                    " );";
        /* package*/ static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
}
