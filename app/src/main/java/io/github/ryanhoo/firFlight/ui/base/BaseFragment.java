package io.github.ryanhoo.firFlight.ui.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import io.github.ryanhoo.firFlight.account.Account;
import io.github.ryanhoo.firFlight.account.AccountManager;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/16/16
 * Time: 12:14 AM
 * Desc: BaseFragment
 */
public abstract class BaseFragment extends Fragment {

    private Account mCurrentAccount;
    private CompositeSubscription mSubscriptions;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCurrentAccount = AccountManager.getCurrentAccount(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Account account = AccountManager.getCurrentAccount(getActivity());
        if (account != null) {
            if (mCurrentAccount == null || !account.getName().equals(mCurrentAccount.getName())) {
                mCurrentAccount = account;
                onAccountChanged();
            }
        }
    }

    protected void updateAccount(Account account) {
        mCurrentAccount = account;
    }

    protected void onAccountChanged() {
        // Account has been changed/switched
    }

    protected void addSubscription(Subscription subscription) {
        if (subscription == null) return;
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        mSubscriptions.add(subscription);
    }
}
