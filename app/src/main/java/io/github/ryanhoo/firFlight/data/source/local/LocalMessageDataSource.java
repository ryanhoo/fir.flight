package io.github.ryanhoo.firFlight.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.data.source.MessageContract;
import io.github.ryanhoo.firFlight.data.source.local.db.tables.MessageTable;
import rx.Observable;
import rx.functions.Func1;

import java.util.List;

import static io.github.ryanhoo.firFlight.data.source.local.db.tables.MessageTable.*;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/2/16
 * Time: 9:26 PM
 * Desc: LocalMessageDataSource
 */
public class LocalMessageDataSource extends AbstractLocalDataSource<MessageTable> implements MessageContract.Local {

    public LocalMessageDataSource(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected MessageTable instantiateTable() {
        return new MessageTable();
    }

    @Override
    public Observable<List<Message>> systemMessages() {
        return mDatabaseHelper.createQuery(TABLE_NAME, QUERY_ALL_SYSTEM_MESSAGES, Message.Type.SYSTEM)
                .mapToList(new Func1<Cursor, Message>() {
                    @Override
                    public Message call(Cursor cursor) {
                        return mTable.parseCursor(cursor);
                    }
                });
    }

    @Override
    public boolean save(Message message) {
        return mDatabaseHelper.insert(TABLE_NAME, mTable.toContentValues(message), SQLiteDatabase.CONFLICT_REPLACE) == 1;
    }

    @Override
    public int save(List<Message> messages) {
        for (Message message : messages) {
            save(message);
        }
        return messages.size();
    }

    @Override
    public boolean delete(Message message) {
        mDatabaseHelper.delete(TABLE_NAME, WHERE_ID_EQUALS, message.getId());
        return true;
    }

    @Override
    public int delete(List<Message> messages) {
        for (Message message : messages) {
            delete(message);
        }
        return messages.size();
    }

    @Override
    public int deleteAll() {
        return mDatabaseHelper.delete(TABLE_NAME, null);
    }
}
