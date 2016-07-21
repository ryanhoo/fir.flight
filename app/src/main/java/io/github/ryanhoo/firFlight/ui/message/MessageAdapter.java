package io.github.ryanhoo.firFlight.ui.message;

import android.content.Context;
import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.ui.common.adapter.ListAdapter;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 8:43 PM
 * Desc: MessageAdapter
 */
public class MessageAdapter extends ListAdapter<Message, MessageItemView> {

    public MessageAdapter(Context context, List<Message> data) {
        super(context, data);
    }

    @Override
    protected MessageItemView createView(Context context) {
        return new MessageItemView(context);
    }
}
