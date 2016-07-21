package io.github.ryanhoo.firFlight.ui.message;

import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.data.source.MessageRepository;
import io.github.ryanhoo.firFlight.network.NetworkSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/21/16
 * Time: 11:23 PM
 * Desc: MessagePresenter
 */
public class MessagePresenter implements MessageContract.Presenter {

    MessageContract.View mView;
    MessageRepository mRepository;
    CompositeSubscription mSubscriptions;

    public MessagePresenter(MessageRepository repository, MessageContract.View view) {
        mRepository = repository;
        mView  = view;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void loadMessages() {
        mView.showLoadingIndicator();
        Subscription subscription = mRepository.systemMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(new NetworkSubscriber<List<Message>>(mView.getContext()){
                    @Override
                    public void onNext(List<Message> messages) {
                        mView.showMessages(messages);
                    }

                    @Override
                    public void onUnsubscribe() {
                        mView.showOrHideEmptyView();
                        mView.hideLoadingIndicator();
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        loadMessages();
    }

    @Override
    public void unsubscribe() {
        mView = null;
        mSubscriptions.clear();
    }
}
