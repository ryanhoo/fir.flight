package io.github.ryanhoo.firFlight.data.source.local;

import android.content.Context;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import io.github.ryanhoo.firFlight.data.source.local.db.DatabaseHelper;
import rx.schedulers.Schedulers;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/26/16
 * Time: 12:22 AM
 * Desc: AbstractLocalDataSource
 */
public abstract class AbstractLocalDataSource {

    protected BriteDatabase mDatabaseHelper;

    public AbstractLocalDataSource(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(databaseHelper, Schedulers.io());
    }
}
