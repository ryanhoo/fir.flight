package io.github.ryanhoo.firFlight.data.source;

import io.github.ryanhoo.firFlight.data.Injection;
import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.data.source.local.LocalMessageDataSource;
import io.github.ryanhoo.firFlight.data.source.remote.RemoteMessageDataSource;
import rx.Observable;
import rx.functions.Action1;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/2/16
 * Time: 9:48 PM
 * Desc: MessageRepository
 */
public class MessageRepository implements MessageContract {

    private static MessageRepository sInstance;

    MessageContract.Local mLocalDataSource;
    MessageContract.Remote mRemoteDataSource;

    private MessageRepository() {
        mLocalDataSource = new LocalMessageDataSource(Injection.provideContext());
        mRemoteDataSource = new RemoteMessageDataSource(Injection.provideRESTfulApi());
    }

    public static MessageRepository getInstance() {
        if (sInstance == null) {
            synchronized (MessageRepository.class) {
                if (sInstance == null) {
                    sInstance = new MessageRepository();
                }
            }
        }
        return sInstance;
    }

    @Override
    public Observable<List<Message>> systemMessages() {
        Observable<List<Message>> local = mLocalDataSource.systemMessages();
        Observable<List<Message>> remote = mRemoteDataSource.systemMessages()
                .doOnNext(new Action1<List<Message>>() {
                    @Override
                    public void call(List<Message> messages) {
                        mLocalDataSource.deleteAll();
                        mLocalDataSource.save(messages);
                    }
                });
        return Observable.concat(local.first(), remote);
    }
}
