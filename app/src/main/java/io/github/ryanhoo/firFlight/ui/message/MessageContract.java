package io.github.ryanhoo.firFlight.ui.message;

import android.content.Context;
import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.ui.base.BasePresenter;
import io.github.ryanhoo.firFlight.ui.base.BaseView;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/21/16
 * Time: 6:06 PM
 * Desc: MessageContract
 */
public interface MessageContract {

    interface View extends BaseView<Presenter> {

        Context getContext();

        void showMessages(List<Message> messages);

        void showOrHideEmptyView();

        void showLoadingIndicator();

        void hideLoadingIndicator();
    }

    interface Presenter extends BasePresenter {

        void loadMessages();
    }
}
