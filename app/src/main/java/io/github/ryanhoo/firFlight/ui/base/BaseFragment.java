package io.github.ryanhoo.firFlight.ui.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import io.github.ryanhoo.firFlight.account.Account;
import io.github.ryanhoo.firFlight.account.AccountManager;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/16/16
 * Time: 12:14 AM
 * Desc: BaseFragment
 */
public abstract class BaseFragment extends Fragment {

    protected Account mCurrentAccount;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCurrentAccount = AccountManager.getCurrentAccount(getActivity());
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

    protected void onAccountChanged() {
        // Account has been changed/switched
    }
}
