package io.github.ryanhoo.firFlight.ui.profile;

import android.util.Log;
import io.github.ryanhoo.firFlight.account.UserSession;
import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.model.User;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 7/9/16
 * Time: 12:32 AM
 * Desc: ProfilePresenter
 */
public class ProfilePresenter implements ProfileContract.Presenter {

    private static final String TAG = "ProfilePresenter";

    private ProfileContract.View mView;
    private CompositeSubscription mSubscriptions;

    public ProfilePresenter(ProfileContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void refreshUserProfile() {
        UserSession.getInstance()
                .user(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(User user) {
                        mView.updateUserProfile(user);
                    }
                });
    }

    @Override
    public void refreshApiToken() {
        Subscription subscription = UserSession.getInstance()
                .refreshApiToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Token>() {
                    @Override
                    public void onStart() {
                        mView.onRefreshApiTokenStart();
                    }

                    @Override
                    public void onCompleted() {
                        mView.onRefreshApiTokenCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(Token token) {
                        mView.updateApiToken(token);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        refreshUserProfile();
    }

    @Override
    public void unsubscribe() {
        mView = null;
        mSubscriptions.clear();
    }
}
