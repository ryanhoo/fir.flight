package io.github.ryanhoo.firFlight.data.source;

import io.github.ryanhoo.firFlight.data.model.Message;
import rx.Observable;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/2/16
 * Time: 9:23 PM
 * Desc: MessageContract
 */
public interface MessageContract {

    interface Local {
        Observable<List<Message>> systemMessages();

        boolean save(Message message);

        int save(List<Message> messages);

        boolean delete(Message message);

        int delete(List<Message> messages);

        int deleteAll();
    }

    interface Remote {
        Observable<List<Message>> systemMessages();
    }

    Observable<List<Message>> systemMessages();
}
