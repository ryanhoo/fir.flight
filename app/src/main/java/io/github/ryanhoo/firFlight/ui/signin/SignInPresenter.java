package io.github.ryanhoo.firFlight.ui.signin;

import android.util.Log;
import io.github.ryanhoo.firFlight.account.UserSession;
import io.github.ryanhoo.firFlight.analytics.FlightAnalytics;
import io.github.ryanhoo.firFlight.analytics.FlightEvent;
import io.github.ryanhoo.firFlight.data.model.User;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 8/13/16
 * Time: 9:55 PM
 * Desc: SignInPresenter
 */
public class SignInPresenter implements SignInContract.Presenter {

    private static final String TAG = "SignInPresenter";

    SignInContract.View mView;
    CompositeSubscription mSubscriptions;

    public SignInPresenter(SignInContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void signIn(final String email, final String password) {
        Subscription subscription = UserSession.getInstance().signIn(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onStart() {
                        mView.onSignInStarted();
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "signIn#onNext: " + user);
                    }

                    @Override
                    public void onCompleted() {
                        // SignIn Event Success
                        FlightAnalytics.onEvent(new FlightEvent(FlightEvent.EVENT_SIGN_IN)
                                .putCustomAttribute(FlightEvent.KEY_EMAIL, email)
                                .putSuccess(true)
                        );

                        mView.onSignInCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // SignIn Event Fail
                        FlightAnalytics.onEvent(new FlightEvent(FlightEvent.EVENT_SIGN_IN)
                                .putCustomAttribute(FlightEvent.KEY_EMAIL, email)
                                .putSuccess(false)
                        );

                        mView.onSignInError(e);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        // Empty
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
        mView = null;
    }
}
